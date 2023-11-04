package com.kakao.sunsuwedding.review;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.BadRequestException;
import com.kakao.sunsuwedding._core.errors.exception.ForbiddenException;
import com.kakao.sunsuwedding._core.errors.exception.NotFoundException;
import com.kakao.sunsuwedding.match.Match;
import com.kakao.sunsuwedding.match.MatchJPARepository;
import com.kakao.sunsuwedding.match.MatchStatus;
import com.kakao.sunsuwedding.match.ReviewStatus;
import com.kakao.sunsuwedding.user.constant.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {
    private final ReviewJPARepository reviewJPARepository;
    private final MatchJPARepository matchJPARepository;

    @Transactional
    public void addReview(String role, Long coupleId, Long chatId, ReviewRequest.AddDTO request) {
        Match match = matchJPARepository.findByChatId(chatId).orElseThrow(
                () -> new NotFoundException(BaseException.MATCHING_NOT_FOUND)
        );

        roleCheck(role); // 예비부부이며
        permissionCheck(coupleId, match); //본인의 매칭이 맞는지 확인
        matchConfirmedCheck(match); // 리뷰 작성 가능한 상태인지 확인

        // 첫 리뷰라면 리뷰 작성 여부 업데이트
        if (match.getReviewStatus().equals(ReviewStatus.UNWRITTEN)) {
            match.updateReviewStatus();
            matchJPARepository.save(match);
        }

        reviewJPARepository.save(
                Review.builder()
                    .match(match)
                    .content(request.content())
                    .build()
        );
    }

    public ReviewResponse.FindAllByPlannerDTO findAllByPlanner(int page, Long plannerId) {
        Pageable pageable = PageRequest.of(page,10);
        Page<Review> pageContent = reviewJPARepository.findAllByMatchPlannerId(plannerId, pageable);
        List<Review> reviews = pageContent.getContent();

        List<ReviewResponse.FindByPlannerDTO> reviewDTOS = ReviewDTOConverter.toFindAllByPlannerDTO(reviews);

        return new ReviewResponse.FindAllByPlannerDTO(reviewDTOS);

    }

    public ReviewResponse.FindAllByCoupleDTO findAllByCouple(String role, Long coupleId) {
        roleCheck(role);

        List<Review> reviews = reviewJPARepository.findAllByMatchCoupleId(coupleId);
        List<ReviewResponse.FindByCoupleDTO> reviewDTOS = ReviewDTOConverter.toFindAllByCoupleDTO(reviews);

        return new ReviewResponse.FindAllByCoupleDTO(reviewDTOS);
    }

    public ReviewResponse.FindByReviewIdDTO findByReviewId(String role, Long coupleId, Long reviewId) {
        Review review = reviewJPARepository.findById(reviewId).orElseThrow(
                () -> new NotFoundException(BaseException.REVIEW_NOT_FOUND)
        );

        roleCheck(role);
        permissionCheck(coupleId,review.getMatch());

        String plannerName = (review.getMatch().getPlanner() != null ) ?
                              review.getMatch().getPlanner().getUsername() : "탈퇴한 사용자";
        return new ReviewResponse.FindByReviewIdDTO(review.getId(), plannerName, review.getContent());
    }

    @Transactional
    public void updateReview(Pair<String, Long> info, Long reviewId, ReviewRequest.UpdateDTO request) {
        Review review = reviewJPARepository.findById(reviewId).orElseThrow(
                () -> new NotFoundException(BaseException.REVIEW_NOT_FOUND)
        );

        roleCheck(info.getFirst());
        permissionCheck(info.getSecond(), review.getMatch());

        review.updateContent(request.content());
        reviewJPARepository.save(review);
    }

    @Transactional
    public void deleteReview(Pair<String, Long> info, Long reviewId) {
        Review review = reviewJPARepository.findById(reviewId).orElseThrow(
                () -> new NotFoundException(BaseException.REVIEW_NOT_FOUND)
        );

        roleCheck(info.getFirst());
        permissionCheck(info.getSecond(), review.getMatch());

        reviewJPARepository.delete(review);
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
}
