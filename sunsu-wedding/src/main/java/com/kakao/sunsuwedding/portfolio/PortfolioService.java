package com.kakao.sunsuwedding.portfolio;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.BadRequestException;
import com.kakao.sunsuwedding._core.errors.exception.ForbiddenException;
import com.kakao.sunsuwedding._core.errors.exception.NotFoundException;
import com.kakao.sunsuwedding._core.utils.PriceCalculator;
import com.kakao.sunsuwedding.favorite.Favorite;
import com.kakao.sunsuwedding.favorite.FavoriteJPARepository;
import com.kakao.sunsuwedding.match.Match;
import com.kakao.sunsuwedding.match.MatchJPARepository;
import com.kakao.sunsuwedding.portfolio.cursor.CursorRequest;
import com.kakao.sunsuwedding.portfolio.cursor.PageCursor;
import com.kakao.sunsuwedding.portfolio.image.ImageEncoder;
import com.kakao.sunsuwedding.portfolio.image.ImageItem;
import com.kakao.sunsuwedding.portfolio.image.ImageItemJPARepository;
import com.kakao.sunsuwedding.portfolio.image.ImageItemService;
import com.kakao.sunsuwedding.portfolio.price.PriceItem;
import com.kakao.sunsuwedding.portfolio.price.PriceItemJDBCRepository;
import com.kakao.sunsuwedding.portfolio.price.PriceItemJPARepository;
import com.kakao.sunsuwedding.quotation.Quotation;
import com.kakao.sunsuwedding.quotation.QuotationJPARepository;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private final FavoriteJPARepository favoriteJPARepository;

    private final ImageItemService imageItemService;

    @Transactional
    public void addPortfolio(PortfolioRequest.AddDTO request, MultipartFile[] images, Long plannerId) {
        // 요청한 플래너 탐색
        Planner planner = findPlannerById(plannerId);

        // 해당 플래너가 생성한 포트폴리오가 이미 있는 경우 예외처리
        Optional<Portfolio> portfolioOptional = portfolioJPARepository.findByPlannerId(plannerId);
        if (portfolioOptional.isPresent())
            throw new BadRequestException(BaseException.PORTFOLIO_ALREADY_EXIST);

        // 포트폴리오 저장
        Long totalPrice = getTotalPrice(request.items());
        Portfolio portfolio = PortfolioDTOConverter.getPortfolioByAdd(planner, totalPrice, request);
        portfolioJPARepository.save(portfolio);

        // 포트폴리오 삭제 후 재등록일 때 이전 거래내역(avg,min,max) 불러오기
        // todo: 재등록일 때도 이전 거래 내역을 초기화해야 하는 거 아닌가요?
        updateConfirmedPrices(planner);

        // 가격 내용 저장
        List<PriceItem> priceItems = PortfolioDTOConverter.getPriceItem(request.items(), portfolio);
        priceItemJDBCRepository.batchInsertPriceItems(priceItems);

        // 이미지 내용 저장
        imageItemService.uploadImage(images, portfolio, planner);
    }

    public PageCursor<List<PortfolioResponse.FindAllDTO>> getPortfolios(CursorRequest request, Long userId) {
        if (!request.hasKey())
            return new PageCursor<>(null, null);

        Pageable pageable = PageRequest
                .ofSize(request.size())
                .withSort(Sort.by("id").descending());
        List<Portfolio> portfolios = search(request, pageable);

        // 더이상 보여줄 포트폴리오가 없다면 커서 null 반환
        if (portfolios.isEmpty())
            return new PageCursor<>(null, null);

        // 커서가 1이거나 NONE_KEY 일 경우 null 로 대체)
        Long nextKey = getNextKey(portfolios);

        List<ImageItem> imageItems = imageItemJPARepository.findAllByThumbnailAndPortfolioInOrderByPortfolioCreatedAtDesc(true, portfolios);
        List<String> encodedImages = ImageEncoder.encode(portfolios, imageItems);
        List<Favorite> favorites = new ArrayList<>();

        // 유저가 존재하는 경우 찜하기 누른 목록 받아옴
        if (userId >= 0)
             favorites = favoriteJPARepository.findByUserIdFetchJoinPortfolio(userId, pageable);

        List<PortfolioResponse.FindAllDTO> data = PortfolioDTOConverter.FindAllDTOConvertor(portfolios, encodedImages, favorites);
        return new PageCursor<>(data, request.next(nextKey).key());
    }

    public PortfolioResponse.FindByIdDTO getPortfolioById(Long portfolioId, Long userId) {

        Portfolio portfolio = portfolioJPARepository.findById(portfolioId).orElseThrow(
                () -> new NotFoundException(BaseException.PORTFOLIO_NOT_FOUND)
        );
        // 플래너 탈퇴 시 조회 X
        if (portfolio.getPlanner() == null)
            throw new NotFoundException(BaseException.PLANNER_NOT_FOUND);

        List<ImageItem> imageItems = imageItemJPARepository.findByPortfolioId(portfolioId);
        List<String> images  = encodeImages(imageItems);
        List<PriceItem> priceItems = priceItemJPARepository.findAllByPortfolioId(portfolioId);

        // 기본적으로 매칭 내역과 견적서에는 빈 배열 할당
        List<Match> matches = new ArrayList<>();
        List<Quotation> quotations = new ArrayList<>();
        boolean isLiked = false, isPremium = false;

        // 프리미엄 등급 유저일 경우 최근 거래 내역 조회를 위한 매칭 내역, 견적서 가져오기
        Optional<User> userOptional = userJPARepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            isLiked = favoriteJPARepository.findByUserAndPortfolio(user.getId(), portfolio.getId()).isPresent();

            if (user.getGrade() == Grade.PREMIUM) {
                isPremium = true;
                matches = matchJPARepository.findLatestTenByPlanner(portfolio.getPlanner());
                List<Long> matchIds = matches.stream().map(Match::getId).toList();
                quotations = quotationJPARepository.findAllByMatchIds(matchIds);
            }
        }

        return PortfolioDTOConverter.FindByIdDTOConvertor(portfolio, images, priceItems, matches, quotations, isLiked, isPremium);
    }

    @Transactional
    public void updatePortfolio(PortfolioRequest.UpdateDTO request, MultipartFile[] images, Long plannerId) {
        Planner planner = findPlannerById(plannerId);
        Portfolio portfolio = findPortfolioByUserId(plannerId);

        portfolio.update(request.plannerName(), request.title(), request.description(),
                request.location(), request.career(), request.partnerCompany(), getTotalPrice(request.items()));

        // 기존의 포트폴리오 가격 항목 일괄 삭제, 특이사항: JPQL 안 쓰니까 DELETE Query N개씩 날아감
        priceItemJPARepository.deleteAllByPortfolioId(portfolio.getId());

        // 업데이트 가격 항목 새로 저장
        List<PriceItem> updatedPriceItems = PortfolioDTOConverter.getPriceItem(request.items(), portfolio);
        priceItemJDBCRepository.batchInsertPriceItems(updatedPriceItems);

        // 이미지 저장
        imageItemService.uploadImage(images, portfolio, planner);
    }

    @Transactional
    public void deletePortfolio(User user) {
        if (!user.getDtype().equals(Role.PLANNER.getRoleName())) {
            throw new ForbiddenException(BaseException.PERMISSION_DENIED_METHOD_ACCESS);
        }
        priceItemJPARepository.deleteAllByPortfolioPlannerId(user.getId());
        imageItemJPARepository.deleteAllByPortfolioPlannerId(user.getId());
        portfolioJPARepository.deleteByPlanner(user);
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

    private Portfolio findPortfolioByUserId(Long plannerId) {
        return portfolioJPARepository.findByPlannerId(plannerId)
                .orElseThrow(() -> new BadRequestException(BaseException.PORTFOLIO_NOT_FOUND));
    }

    private Planner findPlannerById(Long plannerId) {
        return plannerJPARepository.findById(plannerId)
                .orElseThrow(() -> new NotFoundException(BaseException.USER_NOT_FOUND));
    }

    private static Long getTotalPrice(List<PortfolioRequest.ItemDTO> items) {
        return items.stream()
                .mapToLong(PortfolioRequest.ItemDTO::itemPrice)
                .sum();
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

    public void updateConfirmedPrices(Planner planner) {
        List<Match> matches = matchJPARepository.findAllByPlanner(planner);
        Optional<Portfolio> portfolioOptional = portfolioJPARepository.findByPlanner(planner);
        // 포트폴리오, 매칭내역이 존재할 때만 가격 update
        if (portfolioOptional.isPresent() && !matches.isEmpty()) {
            Portfolio portfolio = portfolioOptional.get();

            // 건수, 평균, 최소, 최대 가격 구하기
            Long contractCount = PriceCalculator.getContractCount(matches);
            Long avgPrice = PriceCalculator.calculateAvgPrice(matches, contractCount);
            Long minPrice = PriceCalculator.calculateMinPrice(matches);
            Long maxPrice = PriceCalculator.calculateMaxPrice(matches);

            // portfolio avg,min,max 값 업데이트
            portfolio.updateConfirmedPrices(contractCount, avgPrice, minPrice, maxPrice);
            portfolioJPARepository.save(portfolio);
        }
    }

    private List<String> encodeImages(List<ImageItem> imageItems) {
        return imageItems
                .stream()
                .map(ImageEncoder::encode)
                .toList();
    }
}
