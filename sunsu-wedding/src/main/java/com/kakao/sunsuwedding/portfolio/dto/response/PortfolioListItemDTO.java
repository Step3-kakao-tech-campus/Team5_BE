package com.kakao.sunsuwedding.portfolio.dto.response;

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
