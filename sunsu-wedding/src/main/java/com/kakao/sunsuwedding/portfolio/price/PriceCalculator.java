package com.kakao.sunsuwedding.portfolio.price;

import com.kakao.sunsuwedding.portfolio.PortfolioResponse;

import java.util.List;

public class PriceCalculator {
    public static Long execute(List<PortfolioResponse.PriceItemDTO> priceItemDTOS) {
        return priceItemDTOS.stream().mapToLong(PortfolioResponse.PriceItemDTO::itemPrice).sum();
    }
}
