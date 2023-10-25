package com.kakao.sunsuwedding.portfolio;

import com.kakao.sunsuwedding._core.utils.PriceCalculator;
import com.kakao.sunsuwedding.match.Match;
import com.kakao.sunsuwedding.quotation.Quotation;
import com.kakao.sunsuwedding.portfolio.price.PriceItem;
import com.kakao.sunsuwedding.user.planner.Planner;

import java.util.List;
import java.util.stream.IntStream;

public class PortfolioDTOConverter {
    public static PortfolioResponse.FindByIdDTO FindByIdDTOConvertor(Planner planner,
                                                            Portfolio portfolio,
                                                            List<String> images, List<PriceItem> priceItems,
                                                            List<Match> matches, List<Quotation> quotations) {
        // 가격 항목 DTO 변환
        List<PortfolioResponse.PriceItemDTO> priceItemDTOS = PriceItemDTOConvertor(priceItems);
        Long totalPrice = PriceCalculator.calculatePortfolioPrice(priceItemDTOS);
        PortfolioResponse.PriceDTO priceDTO = new PortfolioResponse.PriceDTO(totalPrice, priceItemDTOS);

        // 거래 내역
        // 일반 회원의 경우 거래내역으로 null 반환
        PortfolioResponse.PaymentHistoryDTO paymentHistoryDTO = null;

        // 프리미엄 회원일 경우 paymentHistory 반환
        if (!matches.isEmpty() && !quotations.isEmpty()) {
            List<PortfolioResponse.PaymentDTO> paymentDTOS = PaymentDTOConvertor(matches, quotations);
            paymentHistoryDTO =
                    new PortfolioResponse.PaymentHistoryDTO(
                    portfolio.getAvgPrice(),
                    portfolio.getMinPrice(),
                    portfolio.getMaxPrice(),
                    paymentDTOS
            );
        }


        return FindByIdDTOConvertor(planner, portfolio, images, priceDTO, paymentHistoryDTO);
    }

    private static PortfolioResponse.FindByIdDTO FindByIdDTOConvertor(Planner planner, Portfolio portfolio, List<String> images,
                                                             PortfolioResponse.PriceDTO priceDTO,
                                                             PortfolioResponse.PaymentHistoryDTO paymentHistoryDTO) {
        return new PortfolioResponse.FindByIdDTO(
                portfolio.getId(),
                planner.getId(),
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

    public static List<PortfolioResponse.FindAllDTO> FindAllDTOConvertor(List<Portfolio> portfolios, List<String> images) {
        return IntStream
                .range(0, portfolios.size())
                .mapToObj(i -> {
                    Portfolio portfolio = portfolios.get(i);
                    return new PortfolioResponse.FindAllDTO(
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

    public static List<PortfolioResponse.PriceItemDTO> PriceItemDTOConvertor(List<PriceItem> priceItems) {
        return priceItems
                .stream()
                .map(priceItem -> new PortfolioResponse.PriceItemDTO(priceItem.getItemTitle(), priceItem.getItemPrice()))
                .toList();
    }

    public static PortfolioResponse.MyPortfolioDTO MyPortfolioDTOConvertor(Planner planner,
                                                                           Portfolio portfolio,
                                                                           List<String> images, List<PriceItem> priceItems) {
        return new PortfolioResponse.MyPortfolioDTO(
                planner.getUsername(),
                images,
                PriceItemDTOConvertor(priceItems),
                portfolio.getTitle(),
                portfolio.getDescription(),
                portfolio.getLocation(),
                portfolio.getCareer(),
                portfolio.getPartnerCompany()
        );
    }

    public static List<PortfolioResponse.PaymentDTO> PaymentDTOConvertor(List<Match> matches, List<Quotation> quotations) {
        return matches.stream()
                .map(match -> {
                    List<Quotation> matchingQuotations = quotations.stream()
                            .filter(quotation ->
                                    quotation.getMatch().getId().equals(match.getId()))
                            .toList();
                    return new PortfolioResponse.PaymentDTO(match.getConfirmedPrice(),
                            match.getConfirmedAt().toString().substring(0, 7), // 월까지만 제공
                            PaymentItemDTOConvertor(matchingQuotations));
                })
                .toList();
    }

    public static List<PortfolioResponse.PaymentItemDTO> PaymentItemDTOConvertor(List<Quotation> quotations) {
        return quotations
                .stream()
                .map(quotation -> new PortfolioResponse.PaymentItemDTO(quotation.getTitle(), quotation.getPrice(),
                        quotation.getCompany(), quotation.getDescription()))
                .toList();
    }

}
