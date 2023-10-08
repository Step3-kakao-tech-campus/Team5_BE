package com.kakao.sunsuwedding.portfolio.dto.request;

import com.kakao.sunsuwedding.portfolio.dto.response.PriceItemDTO;

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
