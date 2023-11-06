package com.kakao.sunsuwedding.review;

import java.util.List;

public class ReviewResponse {
    public record FindAllByPlannerDTO(
            List<FindByPlannerDTO> reviews
    ){}

    public record FindByPlannerDTO(
            Long id,
            String coupleName,
            Integer stars,
            String content,
            List<String> images
    ) {}

    public record FindAllByCoupleDTO(
            List<ReviewDTO> reviews
    ) {}

    public record ReviewDTO(
            Long id,
            String plannerName,
            Integer stars,
            String content,
            List<String> images
    ) {}
}
