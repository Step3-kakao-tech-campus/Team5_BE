package com.kakao.sunsuwedding.portfolio.dto.request;

import com.kakao.sunsuwedding.portfolio.dto.response.PriceItemDTO;

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
