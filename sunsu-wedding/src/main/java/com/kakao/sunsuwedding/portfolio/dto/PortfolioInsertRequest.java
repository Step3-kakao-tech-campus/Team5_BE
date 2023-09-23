package com.kakao.sunsuwedding.portfolio.dto;

import java.util.List;

public record PortfolioInsertRequest(
        String plannerName,
        List<PriceItemDTO> itemPrices,
        String title,
        String description,
        String location,
        String career,
        String partnerCompany
) {
}
