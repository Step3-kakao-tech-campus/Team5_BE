package com.kakao.sunsuwedding.portfolio.dto;

import org.springframework.core.io.Resource;

public record PortfolioDTO(
        Long id,
        Resource[] images,
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
