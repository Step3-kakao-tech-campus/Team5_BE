package com.kakao.sunsuwedding.review;

import java.util.List;

public class ReviewDTOConverter {
    public static List<ReviewResponse.ReviewDTO> toFindAllByUserDTO(List<Review> reviews) {
        return reviews.stream()
                .map(review -> new ReviewResponse.ReviewDTO(review.id, review.content))
                .toList();
    }
}
