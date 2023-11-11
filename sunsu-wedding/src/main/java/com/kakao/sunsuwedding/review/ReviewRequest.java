package com.kakao.sunsuwedding.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public class ReviewRequest {
    public record AddDTO (
            @NotNull(message = "평점은 비어있으면 안됩니다.")
            @Min(value = 1 , message = "최저 평점은 1점입니다.")
            @Max(value = 5,message = "최고 평점은 5점입니다.")
            Integer stars,

            @NotEmpty(message = "내용은 비어있으면 안됩니다.")
            @Length(max = 500, message = "리뷰는 500자까지만 작성 가능합니다.")
            String content,

            @NotNull(message = "이미지는 비어있으면 안됩니다.")
            List<String> images
    ){}

    public record UpdateDTO (
            @NotNull(message = "평점은 비어있으면 안됩니다.")
            @Min(value = 1 , message = "최저 평점은 1점입니다.")
            @Max(value = 5,message = "최고 평점은 5점입니다.")
            Integer stars,

            @NotEmpty(message = "내용은 비어있으면 안됩니다.")
            @Length(max = 500, message = "리뷰는 500자까지만 작성 가능합니다.")
            String content,

            @NotNull(message = "이미지는 비어있으면 안됩니다.")
            List<String> images
    ){}
}
