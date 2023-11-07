package com.kakao.sunsuwedding.review;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.BadRequestException;
import com.kakao.sunsuwedding._core.errors.exception.ForbiddenException;
import com.kakao.sunsuwedding._core.errors.exception.NotFoundException;
import com.kakao.sunsuwedding.match.Match;
import com.kakao.sunsuwedding.match.MatchJPARepository;
import com.kakao.sunsuwedding.match.MatchStatus;
import com.kakao.sunsuwedding.match.ReviewStatus;
import com.kakao.sunsuwedding.portfolio.PortfolioService;
import com.kakao.sunsuwedding.user.base_user.User;
import com.kakao.sunsuwedding.user.constant.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {
    private final ReviewJPARepository reviewJPARepository;
    private final MatchJPARepository matchJPARepository;
    private final PortfolioService portfolioService;

    @Transactional
    public void addReview(User user, Long chatId, ReviewRequest.AddDTO request) {
        Match match = matchJPARepository.findByChatId(chatId).orElseThrow(
                () -> new NotFoundException(BaseException.MATCHING_NOT_FOUND)
        );

        roleCheck(user.getDtype()); // 예비부부이며
        permissionCheck(user.getId(), match); //본인의 매칭이 맞는지 확인
        matchConfirmedCheck(match); // 리뷰 작성 가능한 상태인지 확인

        reviewJPARepository.save(
                Review.builder()
                    .match(match)
                    .stars(request.stars())
                    .content(request.content())
                    .build()
        );

        // 첫 리뷰라면 리뷰 작성 여부 업데이트
        updateReviewStatus(match);
        // 평균 평점 수정
        portfolioService.updateAvgStars(match.getPlanner());
    }

    public ReviewResponse.FindAllByPlannerDTO findReviewsByPlanner(int page, Long plannerId) {
        Pageable pageable = PageRequest.of(page,10);
        Page<Review> pageContent = reviewJPARepository.findAllByMatchPlannerId(plannerId, pageable);
        List<Review> reviews = pageContent.getContent();

        List<ReviewResponse.FindByPlannerDTO> reviewDTOS = ReviewDTOConverter.toFindAllByPlannerDTO(reviews);

        return new ReviewResponse.FindAllByPlannerDTO(reviewDTOS);

    }

    public ReviewResponse.FindAllByCoupleDTO findReviewsByCouple(User user) {
        roleCheck(user.getDtype());

        List<Review> reviews = reviewJPARepository.findAllByMatchCoupleId(user.getId());
        List<ReviewResponse.ReviewDTO> reviewDTOS = ReviewDTOConverter.toFindAllByCoupleDTO(reviews);

        return new ReviewResponse.FindAllByCoupleDTO(reviewDTOS);
    }

    public ReviewResponse.ReviewDTO findReviewById(User user, Long reviewId) {
        Review review = reviewJPARepository.findById(reviewId).orElseThrow(
                () -> new NotFoundException(BaseException.REVIEW_NOT_FOUND)
        );

        roleCheck(user.getDtype());
        permissionCheck(user.getId(),review.getMatch());

        String plannerName = (review.getMatch().getPlanner() != null ) ?
                              review.getMatch().getPlanner().getUsername() : "탈퇴한 사용자";
        return new ReviewResponse.ReviewDTO(review.getId(), plannerName, review.stars, review.getContent());
    }

    @Transactional
    public void updateReview(User user, Long reviewId, ReviewRequest.UpdateDTO request) {
        Review review = reviewJPARepository.findById(reviewId).orElseThrow(
                () -> new NotFoundException(BaseException.REVIEW_NOT_FOUND)
        );

        roleCheck(user.getDtype());
        permissionCheck(user.getId(), review.getMatch());

        review.updateReview(request);

        // 평균 평점 수정
        portfolioService.updateAvgStars(review.getMatch().getPlanner());

        reviewJPARepository.save(review);
    }

    @Transactional
    public void deleteReview(User user, Long reviewId) {
        Review review = reviewJPARepository.findById(reviewId).orElseThrow(
                () -> new NotFoundException(BaseException.REVIEW_NOT_FOUND)
        );
        Match match = review.getMatch();

        roleCheck(user.getDtype());
        permissionCheck(user.getId(), review.getMatch());

        reviewJPARepository.delete(review);

        // 평균 평점 수정
        portfolioService.updateAvgStars(review.getMatch().getPlanner());
        // 삭제 후 리뷰가 1개도 없다면 ReviewStatus UNWRITTEN으로 변경
        if (reviewJPARepository.findAllByMatch(match).isEmpty()) {
            updateReviewStatus(match);
        }
    }

    private void roleCheck(String role) {
        if (role.equals(Role.PLANNER.getRoleName())) {
            throw new ForbiddenException(BaseException.PERMISSION_DENIED_METHOD_ACCESS);
        }
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
}
