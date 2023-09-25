package com.kakao.sunsuwedding.portfolio;

import com.kakao.sunsuwedding.portfolio.dto.PortfolioDTO;
import com.kakao.sunsuwedding.portfolio.dto.PortfolioListItemDTO;
import com.kakao.sunsuwedding.portfolio.dto.PriceDTO;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.IntStream;

public class PortfolioDTOConverter {
    public static PortfolioDTO toPortfolioDTO(Portfolio portfolio, List<Resource> images, PriceDTO priceDTO) {
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

    public static List<PortfolioListItemDTO> toListItemDTO(Page<Portfolio> portfolioPage, List<Resource> images) {
        List<Portfolio> portfolios = portfolioPage.getContent();
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
}
