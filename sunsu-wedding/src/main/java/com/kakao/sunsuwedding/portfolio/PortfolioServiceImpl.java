package com.kakao.sunsuwedding.portfolio;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.BadRequestException;
import com.kakao.sunsuwedding._core.errors.exception.NotFoundException;
import com.kakao.sunsuwedding._core.utils.PriceCalculator;
import com.kakao.sunsuwedding.favorite.Favorite;
import com.kakao.sunsuwedding.favorite.FavoriteJPARepository;
import com.kakao.sunsuwedding.match.Match;
import com.kakao.sunsuwedding.match.MatchJPARepository;
import com.kakao.sunsuwedding.portfolio.cursor.CursorRequest;
import com.kakao.sunsuwedding.portfolio.cursor.PageCursor;
import com.kakao.sunsuwedding.portfolio.image.PortfolioImageItem;
import com.kakao.sunsuwedding.portfolio.image.PortfolioImageItemJPARepository;
import com.kakao.sunsuwedding.portfolio.image.PortfolioImageItemServiceImpl;
import com.kakao.sunsuwedding.portfolio.price.PriceItem;
import com.kakao.sunsuwedding.portfolio.price.PriceItemJDBCRepository;
import com.kakao.sunsuwedding.portfolio.price.PriceItemJPARepository;
import com.kakao.sunsuwedding.quotation.Quotation;
import com.kakao.sunsuwedding.quotation.QuotationJPARepository;
import com.kakao.sunsuwedding.review.Review;
import com.kakao.sunsuwedding.review.ReviewJPARepository;
import com.kakao.sunsuwedding.user.base_user.User;
import com.kakao.sunsuwedding.user.base_user.UserJPARepository;
import com.kakao.sunsuwedding.user.constant.Grade;
import com.kakao.sunsuwedding.user.planner.Planner;
import com.kakao.sunsuwedding.user.planner.PlannerJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PortfolioServiceImpl implements PortfolioService {

    private final PortfolioJPARepository portfolioJPARepository;
    private final PortfolioImageItemJPARepository portfolioImageItemJPARepository;
    private final PriceItemJPARepository priceItemJPARepository;
    private final PriceItemJDBCRepository priceItemJDBCRepository;
    private final MatchJPARepository matchJPARepository;
    private final QuotationJPARepository quotationJPARepository;
    private final PlannerJPARepository plannerJPARepository;
    private final UserJPARepository userJPARepository;
    private final FavoriteJPARepository favoriteJPARepository;
    private final PortfolioImageItemServiceImpl portfolioImageItemServiceImpl;
    private final ReviewJPARepository reviewJPARepository;

    private final PortfolioDTOConverter portfolioDTOConverter;
    private final PriceCalculator priceCalculator;
    private final PortfolioSpecification portfolioSpecification;



    @Transactional
    public void addPortfolio(PortfolioRequest.AddDTO request, Long plannerId) {
        // 요청한 플래너 탐색
        Planner planner = findPlannerByUserIdIfExist(plannerId);

        // 해당 플래너가 생성한 포트폴리오가 이미 있는 경우 예외처리
        checkPortfolioAlreadyExist(plannerId);

        // 포트폴리오 저장
        Long totalPrice = priceCalculator.getRequestTotalPrice(request.items());
        Portfolio portfolio = portfolioDTOConverter.toPortfolioByRequest(planner, totalPrice, request);
        portfolioJPARepository.save(portfolio);

        // 포트폴리오 삭제 후 재등록일 때 이전 거래내역(avg,min,max) 불러오기
        updateConfirmedPrices(planner);

        // 평균 평점 불러오기
        updateAvgStars(planner);

        // 가격 내용 저장
        List<PriceItem> priceItems = portfolioDTOConverter.toPriceItemByPortfolio(request.items(), portfolio);
        priceItemJDBCRepository.batchInsertPriceItems(priceItems);

        // 이미지 내용 저장
        portfolioImageItemServiceImpl.uploadImage(request.images(), portfolio);
    }

    public PageCursor<List<PortfolioResponse.FindAllDTO>> findPortfolios(CursorRequest request, Long userId) {
        if (!request.hasKey())
            return new PageCursor<>(new ArrayList<>(), null);

        Pageable pageable = request.get();
        List<Portfolio> portfolios = findPortfoliosByRequest(request, pageable);

        // 더이상 보여줄 포트폴리오가 없다면 커서 null 반환
        if (portfolios.isEmpty())
            return new PageCursor<>(new ArrayList<>(), null);

        // 커서가 1이거나 NONE_KEY 일 경우 null 로 대체)
        Long nextKey = getNextKey(portfolios);

        List<PortfolioImageItem> portfolioImageItems = portfolioImageItemJPARepository.findAllByThumbnailAndPortfolioInOrderByPortfolioCreatedAtDesc(true, portfolios);
        List<String> imageItems = portfolioImageItems.stream().map(PortfolioImageItem::getImage).toList();
        List<Favorite> favorites = new ArrayList<>();

        // 유저가 존재하는 경우 찜하기 누른 목록 받아옴
        if (userId >= 0)
             favorites = favoriteJPARepository.findByUserIdFetchJoinPortfolio(userId, pageable);

        List<PortfolioResponse.FindAllDTO> data = portfolioDTOConverter.toFindAllDTO(portfolios, imageItems, favorites);
        return new PageCursor<>(data, request.next(nextKey).key());
    }

    public PortfolioResponse.FindByIdDTO findPortfolioById(Long portfolioId, Long userId) {

        Portfolio portfolio = findPortfolioById(portfolioId);

        List<PortfolioImageItem> portfolioImageItems = portfolioImageItemJPARepository.findByPortfolioId(portfolioId);
        List<String> imageItems  = portfolioImageItems.stream().map(PortfolioImageItem::getImage).toList();
        List<PriceItem> priceItems = priceItemJPARepository.findAllByPortfolioId(portfolioId);

        if (userId.equals(-1L)) {
            return portfolioDTOConverter.toFindByIdDTO(portfolio, imageItems, priceItems, null, null, false, false);
        }

        User user = userJPARepository.findById(userId)
                .orElseThrow(() -> new BadRequestException(BaseException.USER_NOT_FOUND));
        boolean isLiked = favoriteJPARepository.findByUserAndPortfolio(user.getId(), portfolio.getId()).isPresent();

        if (user.getGrade().equals(Grade.NORMAL)) {
            return portfolioDTOConverter.toFindByIdDTO(portfolio, imageItems, priceItems, null, null, isLiked, false);
        }

        // 프리미엄 등급 유저일 경우 최근 거래 내역 조회를 위한 매칭 내역, 견적서 가져오기
        List<Match> matches = matchJPARepository.findLatestTenByPlanner(portfolio.getPlanner());
        List<Long> matchIds = matches.stream().map(Match::getId).toList();
        List<Quotation> quotations = quotationJPARepository.findAllByMatchIds(matchIds);

        return portfolioDTOConverter.toFindByIdDTO(portfolio, imageItems, priceItems, matches, quotations, isLiked, true);
    }

    @Transactional
    public void updatePortfolio(PortfolioRequest.UpdateDTO request, Long plannerId) {
        findPlannerByUserIdIfExist(plannerId);
        Portfolio portfolio = findPortfolioByUserId(plannerId);

        portfolio.update(request.plannerName(), request.title(), request.description(),
                request.location(), request.career(), request.partnerCompany(), priceCalculator.getRequestTotalPrice(request.items()));

        // 기존의 포트폴리오 가격 항목 일괄 삭제, 특이사항: JPQL 안 쓰니까 DELETE Query N개씩 날아감
        priceItemJPARepository.deleteAllByPortfolioId(portfolio.getId());

        // 업데이트 가격 항목 새로 저장
        List<PriceItem> updatedPriceItems = portfolioDTOConverter.toPriceItemByPortfolio(request.items(), portfolio);
        priceItemJDBCRepository.batchInsertPriceItems(updatedPriceItems);

        // 이미지 저장
        portfolioImageItemServiceImpl.updateImage(request.images(), portfolio);
    }

    @Transactional
    public void deletePortfolio(User user) {
        priceItemJPARepository.deleteAllByPortfolioPlannerId(user.getId());
        portfolioImageItemJPARepository.deleteAllByPortfolioPlannerId(user.getId());
        portfolioJPARepository.deleteByPlanner(user);
    }

    public PortfolioResponse.MyPortfolioDTO myPortfolio(Long plannerId) {

        // 플래너의 포트폴리오 탐색
        Optional<Portfolio> portfolioOptional  = portfolioJPARepository.findByPlannerId(plannerId);

        // 작성한 포트폴리오가 없으면 null DTO 리턴 - 프론트 요청 사항
        if (portfolioOptional.isEmpty())
            return portfolioDTOConverter.toMyPortfolioDTO();

        Portfolio portfolio = portfolioOptional.get();

        List<PriceItem> priceItems = priceItemJPARepository.findAllByPortfolioId(portfolio.getId());
        List<PortfolioImageItem> portfolioImageItems = portfolioImageItemJPARepository.findByPortfolioId(portfolio.getId());

        if (portfolioImageItems.isEmpty())
            throw new NotFoundException(BaseException.PORTFOLIO_IMAGE_NOT_FOUND);

        List<String> imageItems = portfolioImageItems.stream().map(PortfolioImageItem::getImage).toList();

        return portfolioDTOConverter.toMyPortfolioDTO(portfolio, imageItems, priceItems);
    }


    public void updateConfirmedPrices(Planner planner) {
        List<Match> matches = matchJPARepository.findAllByPlanner(planner);
        Optional<Portfolio> portfolioOptional = portfolioJPARepository.findByPlanner(planner);

        // 포트폴리오, 매칭내역이 존재할 때만 가격 update
        if (portfolioOptional.isEmpty() || matches.isEmpty()) {
            return;
        }

        Portfolio portfolio = portfolioOptional.get();

        // 건수, 평균, 최소, 최대 가격 구하기
        Long contractCount = priceCalculator.getContractCount(matches);
        Long avgPrice = priceCalculator.calculateAvgPrice(matches, contractCount);
        Long minPrice = priceCalculator.calculateMinPrice(matches);
        Long maxPrice = priceCalculator.calculateMaxPrice(matches);

        // portfolio avg,min,max 값 업데이트
        portfolio.updateConfirmedPrices(contractCount, avgPrice, minPrice, maxPrice);
        portfolioJPARepository.save(portfolio);
    }

    public void updateAvgStars(Planner planner) {
        List<Review> reviews = reviewJPARepository.findAllByMatchPlanner(planner);
        Optional<Portfolio> portfolioOptional = portfolioJPARepository.findByPlanner(planner);

        // 포트폴리오, 리뷰가 존재할 때만 평균 평점 update
        if (portfolioOptional.isEmpty() || reviews.isEmpty()) {
            return;
        }

        Portfolio portfolio = portfolioOptional.get();
        Long totalStars = reviews.stream().mapToLong(Review::getStars).sum();
        Long totalCount = reviews.stream().count();
        Double avgStars = totalCount.equals(0L) ? 0.0 : Double.valueOf(totalStars) / totalCount;

        portfolio.updateAvgStars(Double.valueOf(String.format("%.2f", avgStars)));
        portfolioJPARepository.save(portfolio);
    }

    private void checkPortfolioAlreadyExist(Long plannerId) {
        Optional<Portfolio> portfolioOptional = portfolioJPARepository.findByPlannerId(plannerId);
        if (portfolioOptional.isPresent())
            throw new BadRequestException(BaseException.PORTFOLIO_ALREADY_EXIST);
    }

    private Portfolio findPortfolioById(Long portfolioId) {
        Portfolio portfolio = portfolioJPARepository.findById(portfolioId).orElseThrow(
                () -> new NotFoundException(BaseException.PORTFOLIO_NOT_FOUND)
        );

        // 플래너 탈퇴 시 조회 X
        if (portfolio.getPlanner() == null)
            throw new NotFoundException(BaseException.PLANNER_NOT_FOUND);

        return portfolio;
    }

    private Portfolio findPortfolioByUserId(Long plannerId) {
        return portfolioJPARepository.findByPlannerId(plannerId)
                .orElseThrow(() -> new BadRequestException(BaseException.PORTFOLIO_NOT_FOUND));
    }

    private Planner findPlannerByUserIdIfExist(Long plannerId) {
        return plannerJPARepository.findById(plannerId)
                .orElseThrow(() -> new NotFoundException(BaseException.USER_NOT_FOUND));
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

    private List<Portfolio> findPortfoliosByRequest(CursorRequest request, Pageable pageable) {
        Specification<Portfolio> specification = portfolioSpecification.findPortfolio(request);
        return portfolioJPARepository.findAll(specification, pageable).getContent();
    }

}
