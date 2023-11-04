package com.kakao.sunsuwedding.review;

import java.util.List;

public class ReviewResponse {
    public record FindAllByPlannerDTO(
            List<FindByPlannerDTO> reviews
    ){}

    public record FindByPlannerDTO(
            Long id,
            String coupleName,
            // String? Long? stars, 추후 구현
            String content
            // List<String> images 추후 구현
    ) {}

    public record FindAllByCoupleDTO(
            List<FindByCoupleDTO> reviews
    ) {}

    public record FindByCoupleDTO(
            Long id,
            String plannerName,
            // String? Long? stars, 추후 구현
            String content
    ) {}

    public record FindByReviewIdDTO(
            Long id,
            String plannerName,
            // String? Long? stars, 추후 구현
            String content
            // List<String> images 추후 구현
    ) {}
}
