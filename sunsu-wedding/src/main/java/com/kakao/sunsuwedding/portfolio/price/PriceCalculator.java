package com.kakao.sunsuwedding.portfolio.price;

import com.kakao.sunsuwedding.portfolio.dto.PriceItemDTO;

import java.util.List;

public class PriceCalculator {
    public static Long execute(List<PriceItemDTO> priceItemDTOS) {
        return priceItemDTOS.stream().mapToLong(PriceItemDTO::itemPrice).sum();
    }
}
