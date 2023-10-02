package com.kakao.sunsuwedding.portfolio.dto.response;

import java.util.List;

public record PortfolioDTO(
        Long id,
        List<String> images,
        String title,
        String plannerName,
        Long contractCount,
        PriceDTO priceInfo,
        String location,
        String description,
        String career,
        String partnerCompany
) {
}
