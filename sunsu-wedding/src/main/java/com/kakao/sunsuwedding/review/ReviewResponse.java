package com.kakao.sunsuwedding.review;

import java.util.List;

public class ReviewResponse {
    public record FindAllByUserDTO(
            List<ReviewWithNameDTO> reviews
    ){}

    public record ReviewWithNameDTO(
            Long id,
            String username,
            String content
    ) {}

    public record FindAllByChatIdDTO(
            List<ReviewDTO> reviews
    ) {}

    public record ReviewDTO(
            Long id,
            String content
    ) {}
}
