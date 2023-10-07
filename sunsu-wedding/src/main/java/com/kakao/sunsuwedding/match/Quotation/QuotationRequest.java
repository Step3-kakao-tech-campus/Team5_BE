package com.kakao.sunsuwedding.match.Quotation;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class QuotationRequest {
    public record addQuotation(
            @NotNull @Length(min = 2, max = 50)
            String title,
            @NotNull @Min(0)
            Long price,
            String company,
            String description
    ) {}
}
