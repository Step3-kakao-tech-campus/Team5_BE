package com.kakao.sunsuwedding.review;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReviewDTOConverter {
    public ReviewResponse.FindAllByPlannerDTO getFindAllByPlannerDTO(List<Review> reviews, List<String> images) {
        List<ReviewResponse.FindByPlannerDTO> reviewDTOS = reviews.stream()
                .map(review -> new ReviewResponse.FindByPlannerDTO(
                        review.id,
                        (review.getMatch().getCouple() != null) ? review.getMatch().getCouple().getUsername() : "탈퇴한 사용자" ,
                        review.stars,
                        review.content,
                        images
                ))
                .toList();

        return new ReviewResponse.FindAllByPlannerDTO(reviewDTOS);
    }

    public ReviewResponse.FindAllByCoupleDTO getFindAllByCoupleDTO(List<Review> reviews, List<String> images) {
        List<ReviewResponse.ReviewDTO> reviewDTOS = reviews.stream()
                .map(review -> new ReviewResponse.ReviewDTO(
                        review.id,
                        (review.getMatch().getPlanner() != null) ? review.getMatch().getPlanner().getUsername() : "탈퇴한 사용자" ,
                        review.stars,
                        review.content,
                        images
                ))
                .toList();

        return new ReviewResponse.FindAllByCoupleDTO(reviewDTOS);
    }
}