package com.kakao.sunsuwedding.portfolio;

import com.kakao.sunsuwedding.quotation.Quotation;
import com.kakao.sunsuwedding.quotation.QuotationJPARepository;
import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.BadRequestException;
import com.kakao.sunsuwedding._core.errors.exception.ForbiddenException;
import com.kakao.sunsuwedding._core.errors.exception.NotFoundException;
import com.kakao.sunsuwedding._core.utils.PriceCalculator;
import com.kakao.sunsuwedding.match.Match;
import com.kakao.sunsuwedding.match.MatchJPARepository;
import com.kakao.sunsuwedding.match.MatchStatus;
import com.kakao.sunsuwedding.portfolio.cursor.CursorRequest;
import com.kakao.sunsuwedding.portfolio.cursor.PageCursor;
import com.kakao.sunsuwedding.portfolio.image.ImageEncoder;
import com.kakao.sunsuwedding.portfolio.image.ImageItem;
import com.kakao.sunsuwedding.portfolio.image.ImageItemJPARepository;
import com.kakao.sunsuwedding.portfolio.price.PriceItem;
import com.kakao.sunsuwedding.portfolio.price.PriceItemJDBCRepository;
import com.kakao.sunsuwedding.portfolio.price.PriceItemJPARepository;
import com.kakao.sunsuwedding.user.base_user.User;
import com.kakao.sunsuwedding.user.base_user.UserJPARepository;
import com.kakao.sunsuwedding.user.constant.Grade;
import com.kakao.sunsuwedding.user.constant.Role;
import com.kakao.sunsuwedding.user.planner.Planner;
import com.kakao.sunsuwedding.user.planner.PlannerJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PortfolioService {

    private final PortfolioJPARepository portfolioJPARepository;
    private final ImageItemJPARepository imageItemJPARepository;
    private final PriceItemJPARepository priceItemJPARepository;
    private final PriceItemJDBCRepository priceItemJDBCRepository;
    private final MatchJPARepository matchJPARepository;
    private final QuotationJPARepository quotationJPARepository;
    private final PlannerJPARepository plannerJPARepository;
    private final UserJPARepository userJPARepository;

    @Transactional
    public Pair<Portfolio, Planner> addPortfolio(PortfolioRequest.AddDTO request, Long plannerId) {
        // 요청한 플래너 탐색
        Planner planner = plannerJPARepository.findById(plannerId)
                .orElseThrow(() -> new NotFoundException(BaseException.USER_NOT_FOUND));

        Portfolio existPortfolio = portfolioJPARepository.findByPlannerId(plannerId)
                .orElse(new Portfolio());

        // 해당 플래너가 생성한 포트폴리오가 이미 있는 경우 예외처리
        if (existPortfolio.getId() != null)
            throw new BadRequestException(BaseException.PORTFOLIO_ALREADY_EXIST);

        // 필요한 계산값 연산
        Long totalPrice = getTotalPrice(request.getItems());

        // 포트폴리오 엔티티에 저장
        Portfolio portfolio = Portfolio.builder()
                .planner(planner)
                .title(request.getTitle())
                .description(request.getDescription())
                .location(request.getLocation())
                .career(request.getCareer())
                .partnerCompany(request.getPartnerCompany())
                .totalPrice(totalPrice)
                .contractCount(0L)
                .avgPrice(0L)
                .minPrice(0L)
                .maxPrice(0L)
                .build();
        portfolioJPARepository.save(portfolio);

        // 가격 항목 엔티티에 저장
        List<PriceItem> priceItems = getPriceItems(request.getItems(), portfolio);
        priceItemJDBCRepository.batchInsertPriceItems(priceItems);

        // 포트폴리오 삭제 후 재등록일 때 이전 거래내역(avg,min,max) 불러오기
        updateConfirmedPrices(planner);

        // 이미지 처리 로직에 활용하기 위해 포트폴리오 객체 리턴
        return Pair.of(portfolio, planner);
    }

    public PageCursor<List<PortfolioResponse.FindAllDTO>> getPortfolios(CursorRequest request) {
        if (!request.hasKey()) {
            return new PageCursor<>(null, null);
        }

        Pageable pageable = PageRequest
                .ofSize(request.size())
                .withSort(Sort.by("id").descending());
        List<Portfolio> portfolios = search(request, pageable);

        // 더이상 보여줄 포트폴리오가 없다면 커서 null 반환
        if (portfolios.isEmpty()) return new PageCursor<>(null, null);

        // 커서가 1이거나 NONE_KEY 일 경우 null 로 대체)
        Long nextKey = getNextKey(portfolios);

        List<ImageItem> imageItems = imageItemJPARepository.findAllByThumbnailAndPortfolioInOrderByPortfolioCreatedAtDesc(true, portfolios);
        List<String> encodedImages = ImageEncoder.encode(portfolios, imageItems);

        List<PortfolioResponse.FindAllDTO> data = PortfolioDTOConverter.FindAllDTOConvertor(portfolios, encodedImages);
        return new PageCursor<>(data, request.next(nextKey).key());
    }

    private static Long getNextKey(List<Portfolio> portfolios) {
        Long key = portfolios
                .stream()
                .mapToLong(Portfolio::getId)
                .min()
                .orElse(CursorRequest.NONE_KEY);

        if (key.equals(1L) || key.equals(CursorRequest.NONE_KEY)) key = null;
        return key;
    }

    private List<Portfolio> search(CursorRequest request, Pageable pageable) {
        Specification<Portfolio> specification = PortfolioSpecification.findPortfolio(request);
        return portfolioJPARepository.findAll(specification, pageable).getContent();
    }

    public PortfolioResponse.FindByIdDTO getPortfolioById(Long portfolioId, Long userId) {
        // 요청한 유저의 등급을 확인
        User user = userJPARepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(BaseException.USER_NOT_FOUND));
        Grade userGrade = user.getGrade();

        List<ImageItem> imageItems = imageItemJPARepository.findByPortfolioId(portfolioId);
        if (imageItems.isEmpty()) {
            throw new NotFoundException(BaseException.PORTFOLIO_NOT_FOUND);
        }

        Portfolio portfolio = imageItems.get(0).getPortfolio();
        Planner planner = imageItems.get(0).getPortfolio().getPlanner();


        // 플래너 탈퇴 시 조회 X
        if (planner == null) { throw new NotFoundException(BaseException.PLANNER_NOT_FOUND); }

        List<String> images  = encodeImages(imageItems);

        List<PriceItem> priceItems = priceItemJPARepository.findAllByPortfolioId(portfolioId);

        // 기본적으로 매칭 내역과 견적서에는 빈 배열 할당
        List<Match> matches = new ArrayList<>();
        List<Quotation> quotations = new ArrayList<>();

        // 프리미엄 등급 유저일 경우 최근 거래 내역 조회를 위한 매칭 내역, 견적서 가져오기
        if (userGrade == Grade.PREMIUM) {
            matches = matchJPARepository.findLatestTenByPlanner(planner);
            List<Long> matchIds = matches.stream().map(Match::getId).toList();
            quotations = quotationJPARepository.findAllByMatchIds(matchIds);
        }

        return PortfolioDTOConverter.FindByIdDTOConvertor(planner, portfolio, images, priceItems, matches, quotations);
    }

    @Transactional
    public Pair<Portfolio,Planner> updatePortfolio(PortfolioRequest.UpdateDTO request, Long plannerId) {
        // 요청한 플래너 탐색
        Planner planner = plannerJPARepository.findById(plannerId)
                .orElseThrow(() -> new NotFoundException(BaseException.USER_NOT_FOUND));

        // 플래너의 포트폴리오 탐색
        Portfolio portfolio = portfolioJPARepository.findByPlannerId(plannerId)
                .orElseThrow(() -> new BadRequestException(BaseException.PORTFOLIO_NOT_FOUND));

        // 필요한 계산값 연산
        Long totalPrice =  getTotalPrice(request.getItems());

        // 포트폴리오 변경사항 업데이트 객체 생성 (업데이트 쿼리가 마지막 함수 종료될 때 날아가긴 함)
        Portfolio updatedPortfolio = Portfolio.builder()
                .id(portfolio.getId())
                .planner(planner)
                .title(request.getTitle())
                .description(request.getDescription())
                .location(request.getLocation())
                .career(request.getCareer())
                .partnerCompany(request.getPartnerCompany())
                .totalPrice(totalPrice)
                .contractCount(portfolio.getContractCount())
                .avgPrice(portfolio.getAvgPrice())
                .minPrice(portfolio.getMinPrice())
                .maxPrice(portfolio.getMaxPrice())
                .build();
        portfolioJPARepository.save(updatedPortfolio);

        // 기존의 포트폴리오 가격 항목 일괄 삭제
        // 특이사항: JPQL 안 쓰니까 DELETE Query N개씩 날아감
        priceItemJPARepository.deleteAllByPortfolioId(portfolio.getId());

        // 업데이트 가격 항목 새로 저장
        List<PriceItem> updatedPriceItems = getPriceItems(request.getItems(), portfolio);

        priceItemJDBCRepository.batchInsertPriceItems(updatedPriceItems);

        // 삭제 후 삽입 로직으로 변경 ㅠㅠ 열심히 만든건데 못쓰게 되버림 엉엉
        // priceItemJDBCRepository.batchUpdatePriceItems(updatedPriceItems);

        // 이미지 처리 로직에 활용하기 위해 포트폴리오 객체 리턴
        return Pair.of(updatedPortfolio, planner);
    }

    public void updateConfirmedPrices(Planner planner) {
        List<Match> matches = matchJPARepository.findAllByPlanner(planner);
        Optional<Portfolio> portfolioPS = portfolioJPARepository.findByPlanner(planner);
        // 포트폴리오, 매칭내역이 존재할 때만 가격 update
        if (portfolioPS.isPresent() && !matches.isEmpty()) {
            Portfolio portfolio = portfolioPS.get();
            // 건수, 평균, 최소, 최대 가격 구하기
            Long contractCount = matches.stream()
                    .filter(match -> match.getStatus().equals(MatchStatus.CONFIRMED))
                    .count();
            Long avgPrice = PriceCalculator.calculateAvgPrice(matches, contractCount);
            Long minPrice = PriceCalculator.calculateMinPrice(matches);
            Long maxPrice = PriceCalculator.calculateMaxPrice(matches);

            // portfolio avg,min,max 값 업데이트
            portfolio.updateConfirmedPrices(contractCount, avgPrice, minPrice, maxPrice);
            portfolioJPARepository.save(portfolio);
        }
    }

    @Transactional
    public void deletePortfolio(Pair<String, Long> info) {
        if (!info.getFirst().equals(Role.PLANNER.getRoleName())) {
            throw new ForbiddenException(BaseException.PERMISSION_DENIED_METHOD_ACCESS);
        }

        Planner planner = Planner.builder().id(info.getSecond()).build();
        priceItemJPARepository.deleteAllByPortfolioPlannerId(planner.getId());
        imageItemJPARepository.deleteAllByPortfolioPlannerId(planner.getId());
        portfolioJPARepository.deleteByPlanner(planner);
    }


    public PortfolioResponse.MyPortfolioDTO myPortfolio(Long plannerId) {
        // 요청한 플래너 탐색
        Planner planner = plannerJPARepository.findById(plannerId)
                .orElseThrow(() -> new NotFoundException(BaseException.USER_NOT_FOUND));

        // 플래너의 포트폴리오 탐색
        Portfolio portfolio = portfolioJPARepository.findByPlannerId(plannerId)
                .orElseThrow(() -> new BadRequestException(BaseException.PORTFOLIO_NOT_FOUND));

        List<ImageItem> imageItems = imageItemJPARepository.findByPortfolioId(portfolio.getId());
        if (imageItems.isEmpty()) {
            throw new NotFoundException(BaseException.PORTFOLIO_IMAGE_NOT_FOUND);
        }

        List<String> encodedImages = encodeImages(imageItems);

        List<PriceItem> priceItems = priceItemJPARepository.findAllByPortfolioId(portfolio.getId());

        return PortfolioDTOConverter.MyPortfolioDTOConvertor(planner, portfolio, encodedImages, priceItems);
    }

    private List<String> encodeImages(List<ImageItem> imageItems) {
        return imageItems
                .stream()
                .map(ImageEncoder::encode)
                .toList();
    }

    private Long getTotalPrice(List<PortfolioRequest.ItemDTO> items) {
        return items.stream()
                .mapToLong(PortfolioRequest.ItemDTO::getItemPrice)
                .sum();
    }

    private List<PriceItem> getPriceItems(List<PortfolioRequest.ItemDTO> items, Portfolio portfolio) {
        List<PriceItem> priceItems = new ArrayList<>();
        for (PortfolioRequest.ItemDTO item : items) {
            PriceItem priceItem = PriceItem.builder()
                    .portfolio(portfolio)
                    .itemTitle(item.getItemTitle())
                    .itemPrice(item.getItemPrice())
                    .build();
            priceItems.add(priceItem);
        }
        return priceItems;
    }
}
