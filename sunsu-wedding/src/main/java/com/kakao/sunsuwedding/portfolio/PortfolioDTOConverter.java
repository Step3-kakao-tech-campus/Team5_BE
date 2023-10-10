package com.kakao.sunsuwedding.portfolio;

import com.kakao.sunsuwedding._core.utils.PriceCalculator;
import com.kakao.sunsuwedding.match.Match;
import com.kakao.sunsuwedding.match.Quotation.Quotation;
import com.kakao.sunsuwedding.portfolio.price.PriceItem;

import java.util.List;
import java.util.stream.IntStream;

public class PortfolioDTOConverter {
    public static PortfolioResponse.findById toPortfolioDTO(Portfolio portfolio,
                                                            List<String> images, List<PriceItem> priceItems,
                                                            List<Match> matches, List<Quotation> quotations) {
        List<PortfolioResponse.PriceItemDTO> priceItemDTOS = toPriceItemDTOS(priceItems);

        Long totalPrice = PriceCalculator.calculatePortfolioPrice(priceItemDTOS);
        PortfolioResponse.PriceDTO priceDTO = new PortfolioResponse.PriceDTO(totalPrice, priceItemDTOS);

        // 거래 내역
        List<PortfolioResponse.PaymentDTO> paymentDTOS = toPaymentDTOS(matches, quotations);
        PortfolioResponse.PaymentHistoryDTO paymentHistoryDTO =
                new PortfolioResponse.PaymentHistoryDTO(portfolio.getAvgPrice(), portfolio.getMinPrice(),
                        portfolio.getMaxPrice(), paymentDTOS);

        return toPortfolioDTO(portfolio, images, priceDTO, paymentHistoryDTO);
    }

    private static PortfolioResponse.findById toPortfolioDTO(Portfolio portfolio, List<String> images,
                                                             PortfolioResponse.PriceDTO priceDTO,
                                                             PortfolioResponse.PaymentHistoryDTO paymentHistoryDTO) {
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
                portfolio.getPartnerCompany(),
                paymentHistoryDTO
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

    public static List<PortfolioResponse.PaymentItemDTO> toPaymentItemDTOS(List<Quotation> quotations) {
        return quotations
                .stream()
                .map(quotation -> new PortfolioResponse.PaymentItemDTO(quotation.getTitle(), quotation.getPrice(),
                        quotation.getCompany(), quotation.getDescription()))
                .toList();
    }

    public static List<PortfolioResponse.PaymentDTO> toPaymentDTOS(List<Match> matches, List<Quotation> quotations) {
        return matches.stream()
                .map(match -> {
                    List<Quotation> matchingQuotations = quotations.stream()
                            .filter(quotation ->
                                    quotation.getMatch().getId().equals(match.getId()))
                            .toList();
                    return new PortfolioResponse.PaymentDTO(match.getConfirmedPrice(),
                            match.getConfirmed_at().toString().substring(0, 7), // 월까지만 제공
                            toPaymentItemDTOS(matchingQuotations));
                })
                .toList();
    }
}
