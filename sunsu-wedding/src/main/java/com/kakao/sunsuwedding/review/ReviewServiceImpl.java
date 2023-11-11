package com.kakao.sunsuwedding.review;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.BadRequestException;
import com.kakao.sunsuwedding._core.errors.exception.ForbiddenException;
import com.kakao.sunsuwedding._core.errors.exception.NotFoundException;
import com.kakao.sunsuwedding.match.Match;
import com.kakao.sunsuwedding.match.MatchJPARepository;
import com.kakao.sunsuwedding.match.MatchStatus;
import com.kakao.sunsuwedding.match.ReviewStatus;
import com.kakao.sunsuwedding.portfolio.Portfolio;
import com.kakao.sunsuwedding.portfolio.PortfolioJPARepository;
import com.kakao.sunsuwedding.portfolio.PortfolioServiceImpl;
import com.kakao.sunsuwedding.review.image.ReviewImageItem;
import com.kakao.sunsuwedding.review.image.ReviewImageItemJPARepository;
import com.kakao.sunsuwedding.review.image.ReviewImageItemService;
import com.kakao.sunsuwedding.user.base_user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {
    private final ReviewJPARepository reviewJPARepository;
    private final MatchJPARepository matchJPARepository;
    private final ReviewImageItemJPARepository reviewImageItemJPARepository;
    private final ReviewImageItemService reviewImageItemService;
    private final PortfolioServiceImpl portfolioServiceImpl;
    private final PortfolioJPARepository portfolioJPARepository;

    private final ReviewDTOConverter reviewDTOConverter;

    private final static int PAGE_SIZE = 10;

    @Transactional
    public void addReview(User user, Long chatId, ReviewRequest.AddDTO request) {
        Match match = findMatchByChatId(chatId);

        // 본인의 매칭이 맞는지 확인
        permissionCheck(user.getId(), match);
        // 전체 확정된 매칭인지 확인
        matchConfirmedCheck(match);
        // 이전에 review 작성있는지 확인
        reviewExistCheck(match);

        Review review = Review.builder()
                .match(match)
                .stars(request.stars())
                .content(request.content())
                .build();
        reviewJPARepository.save(review);

        // 첫 리뷰라면 리뷰 작성 여부 업데이트
        updateReviewStatus(match);

        // 평균 평점 수정
        portfolioServiceImpl.updateAvgStars(match.getPlanner());

        // 리뷰 이미지 저장
        reviewImageItemService.uploadImage(request.images(),review);
    }

    public ReviewResponse.FindAllByPlannerDTO findReviewsByPlanner(int page, Long plannerId) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<Review> pageContent = reviewJPARepository.findAllByMatchPlannerId(plannerId, pageable);

        List<Review> reviews = pageContent.getContent();
        List<Long> reviewIds = reviews.stream().map(Review::getId).toList();
        List<ReviewImageItem> reviewImageItems = reviewImageItemJPARepository.findAllByReviewIds(reviewIds);

        return reviewDTOConverter.getFindAllByPlannerDTO(reviews, reviewImageItems);
    }

    public ReviewResponse.FindAllByCoupleDTO findReviewsByCouple(User user) {

        List<Review> reviews = reviewJPARepository.findAllByMatchCoupleId(user.getId());
        List<Long> reviewIds = reviews.stream().map(Review::getId).toList();
        List<ReviewImageItem> reviewImageItems = reviewImageItemJPARepository.findAllByReviewIds(reviewIds);
        return reviewDTOConverter.getFindAllByCoupleDTO(reviews, reviewImageItems);
    }

    public ReviewResponse.ReviewDTO findReviewById(User user, Long reviewId) {
        Review review = findReviewById(reviewId);

        permissionCheck(user.getId(),review.getMatch());

        String plannerName = getNameByUser(review.getMatch().getPlanner());
        String coupleName = getNameByUser(review.match.getCouple());

        Optional<Portfolio> portfolio = portfolioJPARepository.findByPlanner(review.getMatch().getPlanner());
        Long portfolioId = portfolio.isPresent() ? portfolio.get().getId() : -1L;

        List<String> images = reviewImageItemJPARepository.findByReviewId(reviewId);

        return new ReviewResponse.ReviewDTO(review.getId(), portfolioId, plannerName, coupleName, review.stars, review.getContent(), images);
    }

    @Transactional
    public void updateReview(User user, Long reviewId, ReviewRequest.UpdateDTO request) {
        Review review = findReviewById(reviewId);

        permissionCheck(user.getId(), review.getMatch());

        review.updateReview(request);

        // 평균 평점 수정
        portfolioServiceImpl.updateAvgStars(review.getMatch().getPlanner());

        reviewJPARepository.save(review);

        // 리뷰 이미지 수정
        reviewImageItemService.updateImage(request.images(), review);
    }

    @Transactional
    public void deleteReview(User user, Long reviewId) {
        Review review = findReviewById(reviewId);
        Match match = review.getMatch();

        permissionCheck(user.getId(), review.getMatch());

        reviewJPARepository.delete(review);

        // 평균 평점 수정
        portfolioServiceImpl.updateAvgStars(review.getMatch().getPlanner());

        // ReviewStatus UNWRITTEN으로 변경
        updateReviewStatus(match);

        reviewImageItemJPARepository.deleteAllByReviewId(reviewId);
    }

    private Match findMatchByChatId(Long chatId) {
        return matchJPARepository.findByChatId(chatId).orElseThrow(
                () -> new NotFoundException(BaseException.MATCHING_NOT_FOUND)
        );
    }

    private Review findReviewById(Long reviewId) {
        return reviewJPARepository.findById(reviewId).orElseThrow(
                () -> new NotFoundException(BaseException.REVIEW_NOT_FOUND)
        );
    }

    private void permissionCheck(Long userId, Match match) {
        if (!match.getCouple().getId().equals(userId)) {
            throw new ForbiddenException(BaseException.PERMISSION_DENIED_METHOD_ACCESS);
        }
    }

    private void matchConfirmedCheck(Match match) {
        if (!match.getStatus().equals(MatchStatus.CONFIRMED)) {
            throw new BadRequestException(BaseException.MATCHING_NOT_CONFIRMED);
        }
    }

    private void reviewExistCheck(Match match){
        if (match.getReviewStatus().equals(ReviewStatus.WRITTEN)){
            throw new BadRequestException(BaseException.REVIEW_EXIST);
        }
    }

    private void updateReviewStatus(Match match){
        if (match.getReviewStatus().equals(ReviewStatus.UNWRITTEN)) {
            match.updateReviewStatus(ReviewStatus.WRITTEN);
            matchJPARepository.save(match);
        }
        else {
            match.updateReviewStatus(ReviewStatus.UNWRITTEN);
            matchJPARepository.save(match);
        }
    }

    private static String getNameByUser(User user) {
        if (user == null) {
            return "탈퇴한 사용자";
        }
        return user.getUsername();
    }
}
