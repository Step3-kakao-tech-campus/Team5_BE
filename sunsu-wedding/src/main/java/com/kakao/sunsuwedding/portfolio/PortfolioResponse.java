package com.kakao.sunsuwedding.portfolio;

import java.util.List;

public class PortfolioResponse {
    public record findAllBy(
            Long id,
            String image,
            String title,
            String plannerName,
            Long price,
            String location,
            Long contractCount
    ) {
    }

    public record findById(
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

    public record PriceDTO(
            Long totalPrice,
            List<PriceItemDTO> itemPrices
    ) {
    }

    public record PriceItemDTO(
            String itemTitle,
            Long itemPrice
    ) {
    }
}
