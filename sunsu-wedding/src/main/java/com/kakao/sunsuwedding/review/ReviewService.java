package com.kakao.sunsuwedding.review;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.NotFoundException;
import com.kakao.sunsuwedding.user.constant.Role;
import com.kakao.sunsuwedding.user.couple.Couple;
import com.kakao.sunsuwedding.user.couple.CoupleJPARepository;
import com.kakao.sunsuwedding.user.planner.Planner;
import com.kakao.sunsuwedding.user.planner.PlannerJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewJPARepository reviewJPARepository;
    private final PlannerJPARepository plannerJPARepository;
    private final CoupleJPARepository coupleJPARepository;

    @Transactional
    public void addReview(Pair<String, Long> info, ReviewRequest.AddDTO request) {
        Planner planner = plannerJPARepository.findById(request.plannerId()).orElseThrow(
                () -> new NotFoundException(BaseException.PLANNER_NOT_FOUND));

        Couple couple = coupleJPARepository.findById(info.getSecond()).orElseThrow(
                () -> new NotFoundException(BaseException.USER_NOT_FOUND)
        );

        reviewJPARepository.save(
                Review.builder()
                    .planner(planner)
                    .couple(couple)
                    .content(request.content())
                    .build()
        );
    }

    public ReviewResponse.ReviewDTO findByReviewId(Long reviewId) {
        Review review = reviewJPARepository.findById(reviewId).orElseThrow(
                () -> new NotFoundException(BaseException.REVIEW_NOT_FOUND)
        );

        return new ReviewResponse.ReviewDTO(review.id, review.content);
    }

    public ReviewResponse.FindAllByUserDTO findAllByUser(Pair<String, Long> info) {
        String role = info.getFirst();
        Long userId = info.getSecond();

        List<Review> reviews;
        if (role.equals(Role.PLANNER)) {
            reviews = reviewJPARepository.findAllByPlannerId(userId);
        }
        else {
            reviews = reviewJPARepository.findAllByCoupleId(userId);
        }

        List<ReviewResponse.ReviewDTO> reviewDTOS = ReviewDTOConverter.toFindAllByUserDTO(reviews);

        return new ReviewResponse.FindAllByUserDTO(reviewDTOS);
    }
}
