package com.kakao.sunsuwedding.portfolio.dto;

import org.springframework.core.io.Resource;

import java.util.List;

public record PortfolioDTO(
        Long id,
        List<Resource> images,
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
