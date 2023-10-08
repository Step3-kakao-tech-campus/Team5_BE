package com.kakao.sunsuwedding.match.Quotation;

import java.time.LocalDateTime;
import java.util.List;

public class QuotationResponse {
    public record findAllByMatchId(
            QuotationStatus status,
            List<QuotationDTO> quotations
    ) {}

    public record QuotationDTO(
            Long id,
            String title,
            Long price,
            String company,
            String description,
            QuotationStatus status,
            LocalDateTime modifiedAt
    ) {}
}
