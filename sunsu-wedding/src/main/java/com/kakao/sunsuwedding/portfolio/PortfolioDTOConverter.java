package com.kakao.sunsuwedding.portfolio;

import com.kakao.sunsuwedding.portfolio.price.PriceCalculator;
import com.kakao.sunsuwedding.portfolio.price.PriceItem;

import java.util.List;
import java.util.stream.IntStream;

public class PortfolioDTOConverter {
    public static PortfolioResponse.findById toPortfolioDTO(Portfolio portfolio, List<String> images, List<PriceItem> priceItems) {
        List<PortfolioResponse.PriceItemDTO> priceItemDTOS = toPriceItemDTOS(priceItems);

        Long totalPrice = PriceCalculator.calculatePortfolioPrice(priceItemDTOS);
        PortfolioResponse.PriceDTO priceDTO = new PortfolioResponse.PriceDTO(totalPrice, priceItemDTOS);

        return toPortfolioDTO(portfolio, images, priceDTO);
    }

    private static PortfolioResponse.findById toPortfolioDTO(Portfolio portfolio, List<String> images, PortfolioResponse.PriceDTO priceDTO) {
        return new PortfolioResponse.findById(
                portfolio.getId(),
                images,
                portfolio.getTitle(),
                portfolio.getPlanner().getUsername(),
                portfolio.getContractCount(),
                priceDTO,
                portfolio.getLocation(),
                portfolio.getDescription(),
                portfolio.getCareer(),
                portfolio.getPartnerCompany()
        );
    }

    public static List<PortfolioResponse.findAllBy> toListItemDTO(List<Portfolio> portfolios, List<String> images) {
        return IntStream
                .range(0, portfolios.size())
                .mapToObj(i -> {
                    Portfolio portfolio = portfolios.get(i);
                    return new PortfolioResponse.findAllBy(
                            portfolio.getId(),
                            images.get(i),
                            portfolio.getTitle(),
                            portfolio.getPlanner().getUsername(),
                            portfolio.getTotalPrice(),
                            portfolio.getLocation(),
                            portfolio.getContractCount()
                    );
                })
                .toList();
    }

    public static List<PortfolioResponse.PriceItemDTO> toPriceItemDTOS(List<PriceItem> priceItems) {
        return priceItems
                .stream()
                .map(priceItem -> new PortfolioResponse.PriceItemDTO(priceItem.getItemTitle(), priceItem.getItemPrice()))
                .toList();
    }
}
