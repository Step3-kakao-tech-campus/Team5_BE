package com.kakao.sunsuwedding.review;

import com.kakao.sunsuwedding.user.planner.Planner;
import jakarta.validation.constraints.NotNull;

public class ReviewRequest {
    public record AddDTO (
            @NotNull(message = "plannerId는 비어있으면 안됩니다.")
            Long plannerId,

            @NotNull(message = "content는 비어있으면 안됩니다.")
            String content
    ){}
}
