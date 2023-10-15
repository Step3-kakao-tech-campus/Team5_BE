package com.kakao.sunsuwedding.match.Quotation;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class QuotationRequest {
    public record Add(
            @NotNull(message = "견적 제목은 비어있으면 안됩니다.")
            @Length(min = 2, max = 50, message = "견적 제목은 2자 이상 50자 이하만 가능합니다.")
            String title,

            @NotNull(message = "가격은 비어있으면 안됩니다.")
            @Min(value = 0, message = "가격은 0원 이상만 가능합니다.")
            Long price,

            @Length(max = 50, message = "업체명은 50자 이하만 가능합니다.")
            String company,

            @Length(max = 500, message = "상세설명은 500자 이하만 가능합니다.")
            String description
    ) {}

    public record Update(
            @NotNull(message = "견적 제목은 비어있으면 안됩니다.")
            @Length(min = 2, max = 50, message = "견적 제목은 2자 이상 50자 이하만 가능합니다.")
            String title,

            @NotNull(message = "가격은 비어있으면 안됩니다.")
            @Min(value = 0, message = "가격은 0원 이상만 가능합니다.")
            Long price,

            @Length(max = 50, message = "업체명은 50자 이하만 가능합니다.")
            String company,

            @Length(max = 500, message = "상세설명은 500자 이하만 가능합니다.")
            String description
    ) {}
}
