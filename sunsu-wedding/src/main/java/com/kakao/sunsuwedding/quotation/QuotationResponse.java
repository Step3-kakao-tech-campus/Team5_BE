package com.kakao.sunsuwedding.quotation;

import java.time.LocalDateTime;
import java.util.List;

public class QuotationResponse {
    public record FindAllByMatchId(
            String status,
            Long totalPrice,
            Long confirmedPrice,
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