package com.kakao.sunsuwedding.review;

import com.kakao.sunsuwedding.review.image.ReviewImageItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReviewDTOConverter {
    public ReviewResponse.FindAllByPlannerDTO getFindAllByPlannerDTO(List<Review> reviews, List<ReviewImageItem> reviewImageItems) {
        List<ReviewResponse.FindByUserDTO> reviewDTOS = reviews.stream()
                .map(review -> new ReviewResponse.FindByUserDTO(
                        review.id,
                        (review.getMatch().getCouple() != null) ? review.getMatch().getCouple().getUsername() : "탈퇴한 사용자" ,
                        review.stars,
                        review.content,
                        reviewImageItems.stream()
                                .filter(reviewImageItem -> reviewImageItem.getReview().getId().equals(review.id))
                                .map(ReviewImageItem::getImage)
                                .toList()
                ))
                .toList();

        return new ReviewResponse.FindAllByPlannerDTO(reviewDTOS);
    }

    public ReviewResponse.FindAllByCoupleDTO getFindAllByCoupleDTO(List<Review> reviews, List<ReviewImageItem> reviewImageItems) {
        List<ReviewResponse.FindByPlannerDTO> reviewDTOS = reviews.stream()
                .map(review -> new ReviewResponse.FindByPlannerDTO(
                        review.id,
                        (review.getMatch().getPlanner() != null) ? review.getMatch().getPlanner().getUsername() : "탈퇴한 사용자" ,
                        review.stars,
                        review.content,
                        reviewImageItems.stream()
                                .filter(reviewImageItem -> reviewImageItem.getReview().getId().equals(review.id))
                                .map(ReviewImageItem::getImage)
                                .toList()
                ))
                .toList();

        return new ReviewResponse.FindAllByCoupleDTO(reviewDTOS);
    }
}