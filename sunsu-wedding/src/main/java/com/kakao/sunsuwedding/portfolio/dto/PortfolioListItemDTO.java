package com.kakao.sunsuwedding.portfolio.dto;

public record PortfolioListItemDTO(
        Long id,
        String image,
        String title,
        String plannerName,
        Long price,
        String location,
        Long contractCount
) {
}
