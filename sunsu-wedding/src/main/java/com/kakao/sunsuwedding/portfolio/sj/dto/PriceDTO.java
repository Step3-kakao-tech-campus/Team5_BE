package com.kakao.sunsuwedding.portfolio.dto;

import java.util.List;

public record PriceDTO(
        Long totalPrice,
        List<PriceItemDTO> itemPrices
) {
}
