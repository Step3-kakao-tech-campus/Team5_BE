package com.kakao.sunsuwedding.review;

import java.util.List;

public class ReviewResponse {
    public record FindAllByPlannerDTO(
            List<FindByUserDTO> reviews
    ) {}

    public record FindAllByCoupleDTO(
            List<FindByPlannerDTO> reviews
    ) {}

    public record FindByUserDTO(
            Long id,
            String coupleName,
            Integer stars,
            String content,
            List<String> images
    ) {}

    public record FindByPlannerDTO(
            Long id,
            String plannerName,
            Integer stars,
            String content,
            List<String> images
    ) {}

    public record ReviewDTO(
            Long id,
            Long portfolioId,
            String plannerName,
            String coupleName,
            Integer stars,
            String content,
            List<String> images
    ) {}
}
