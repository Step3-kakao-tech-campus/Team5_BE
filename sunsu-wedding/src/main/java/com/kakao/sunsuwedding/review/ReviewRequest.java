package com.kakao.sunsuwedding.review;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class ReviewRequest {
    public record AddDTO (
            // 평점 추가
            @NotNull(message = "content는 비어있으면 안됩니다.")
            @Length(max = 500, message = "리뷰는 500자까지만 작성 가능합니다.")
            String content
    ){}

    public record UpdateDTO (
            @NotNull(message = "content는 비어있으면 안됩니다.")
            @Length(max = 500, message = "리뷰는 500자까지만 작성 가능합니다.")
            String content
    ){}

    public record FindAllByPlannerDTO(
            @NotNull(message = "plannerId는 비어있으면 안됩니다.")
            Long plannerId
    ) {}
}
