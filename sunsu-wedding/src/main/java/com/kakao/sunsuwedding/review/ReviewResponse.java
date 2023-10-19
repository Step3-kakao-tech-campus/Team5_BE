package com.kakao.sunsuwedding.review;

import java.util.List;

public class ReviewResponse {
    public record FindAllByUserDTO(
            List<ReviewDTO> reviews
    ){}

    public record ReviewDTO(
            Long id,
            String content
    ) {}
}
