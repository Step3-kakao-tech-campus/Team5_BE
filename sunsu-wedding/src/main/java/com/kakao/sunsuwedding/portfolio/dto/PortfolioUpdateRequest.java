package com.kakao.sunsuwedding.portfolio.dto;

import java.util.List;

public record PortfolioUpdateRequest(
        String plannerName,
        String title,
        String description,
        String location,
        String career,
        String partnerCompany,
        List<PriceItemDTO> itemPrices
) {
}
