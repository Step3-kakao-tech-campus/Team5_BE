package com.kakao.sunsuwedding.review;

import com.kakao.sunsuwedding.user.base_user.User;

public interface ReviewService {

    void addReview(User user, Long chatId, ReviewRequest.AddDTO request);

    ReviewResponse.FindAllByPlannerDTO findReviewsByPlanner(int page, Long plannerId);

    ReviewResponse.FindAllByCoupleDTO findReviewsByCouple(User user);

    ReviewResponse.ReviewDTO findReviewById(User user, Long reviewId);

    void updateReview(User user, Long reviewId, ReviewRequest.UpdateDTO request);

    void deleteReview(User user, Long reviewId);
}
