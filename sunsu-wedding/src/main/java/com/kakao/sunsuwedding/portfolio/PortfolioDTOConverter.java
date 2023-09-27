package com.kakao.sunsuwedding.portfolio;

import com.kakao.sunsuwedding.portfolio.dto.PortfolioDTO;
import com.kakao.sunsuwedding.portfolio.dto.PortfolioListItemDTO;
import com.kakao.sunsuwedding.portfolio.dto.PriceDTO;
import com.kakao.sunsuwedding.portfolio.dto.PriceItemDTO;
import com.kakao.sunsuwedding.portfolio.price.PriceItem;

import java.util.List;
import java.util.stream.IntStream;

public class PortfolioDTOConverter {
    public static PortfolioDTO toPortfolioDTO(Portfolio portfolio, List<String> images, PriceDTO priceDTO) {
        return new PortfolioDTO(
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

    public static List<PortfolioListItemDTO> toListItemDTO(List<Portfolio> portfolios, List<String> images) {
        return IntStream
                .range(0, portfolios.size())
                .mapToObj(i -> {
                    Portfolio portfolio = portfolios.get(i);
                    return new PortfolioListItemDTO(
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

    public static List<PriceItemDTO> toPriceItemDTOS(List<PriceItem> priceItems) {
        return priceItems
                .stream()
                .map(priceItem -> new PriceItemDTO(priceItem.getItemTitle(), priceItem.getItemPrice()))
                .toList();
    }
}