package com.kakao.sunsuwedding.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class ReviewRequest {
    public record AddDTO (
            @NotNull(message = "평점은 비어있으면 안됩니다.")
            @Min(value = 1 , message = "최저 평점은 1점입니다.")
            @Max(value = 5,message = "최고 평점은 5점입니다.")
            Integer stars,
            @NotNull(message = "content는 비어있으면 안됩니다.")
            @Length(max = 500, message = "리뷰는 500자까지만 작성 가능합니다.")
            String content
    ){}

    public record UpdateDTO (
            @NotNull(message = "평점은 비어있으면 안됩니다.")
            @Min(value = 1 , message = "최저 평점은 1점입니다.")
            @Max(value = 5,message = "최고 평점은 5점입니다.")
            Integer stars,
            @NotNull(message = "content는 비어있으면 안됩니다.")
            @Length(max = 500, message = "리뷰는 500자까지만 작성 가능합니다.")
            String content
    ){}

    public record FindAllByPlannerDTO(
            @NotNull(message = "plannerId는 비어있으면 안됩니다.")
            Long plannerId
    ) {}
}
