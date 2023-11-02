package com.kakao.sunsuwedding.review;

import java.util.List;

public class ReviewDTOConverter {
    public static List<ReviewResponse.ReviewWithNameDTO> toFindAllByPlannerDTO(List<Review> reviews) {
        return reviews.stream()
                .map(review -> new ReviewResponse.ReviewWithNameDTO(
                        review.id,
                        (review.getMatch().getCouple() != null) ? review.getMatch().getCouple().getUsername() : "탈퇴한 사용자" ,
                        review.content)
                )
                .toList();
    }

    public static List<ReviewResponse.ReviewWithNameDTO> toFindAllByCoupleDTO(List<Review> reviews) {
        return reviews.stream()
                .map(review -> new ReviewResponse.ReviewWithNameDTO(
                        review.id,
                        (review.getMatch().getPlanner() != null) ? review.getMatch().getPlanner().getUsername() : "탈퇴한 사용자" ,
                        review.content)
                )
                .toList();
    }

    public static List<ReviewResponse.ReviewDTO> toFindAllByChatIdDTO(List<Review> reviews) {
        return reviews.stream()
                .map(review -> new ReviewResponse.ReviewDTO(review.id, review.content))
                .toList();
    }
}
