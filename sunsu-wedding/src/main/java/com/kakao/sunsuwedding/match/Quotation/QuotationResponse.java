package com.kakao.sunsuwedding.match.Quotation;

import java.time.LocalDateTime;
import java.util.List;

public class QuotationResponse {
    public record findAllByMatchId(
            String status,
            List<QuotationDTO> quotations
    ) {}

    public record QuotationDTO(
            Long id,
            String title,
            Long price,
            String company,
            String description,
            String status,
            LocalDateTime modifiedAt
    ) {}
}
