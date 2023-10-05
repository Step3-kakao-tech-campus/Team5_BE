package com.kakao.sunsuwedding.match.Quotation;

public class QuotationRequest {
    public record addQuotation(
            String title,
            Long price,
            String company,
            String description
    ) {}
}
