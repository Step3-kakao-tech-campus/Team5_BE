package com.kakao.sunsuwedding.portfolio.dto;

import org.springframework.core.io.Resource;

public record PortfolioListItemDTO(
        Long id,
        Resource image,
        String title,
        String plannerName,
        Long price,
        String location,
        Long contractCount
) {
}
