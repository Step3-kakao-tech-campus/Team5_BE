package com.kakao.sunsuwedding.portfolio;

import com.kakao.sunsuwedding._core.utils.PriceCalculator;
import com.kakao.sunsuwedding.favorite.Favorite;
import com.kakao.sunsuwedding.match.Match;
import com.kakao.sunsuwedding.quotation.Quotation;
import com.kakao.sunsuwedding.portfolio.price.PriceItem;
import com.kakao.sunsuwedding.user.planner.Planner;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class PortfolioDTOConverter {

    public static Portfolio getPortfolioByAdd(Planner planner, Long totalPrice, PortfolioRequest.AddDTO request){
        return Portfolio.builder()
                .planner(planner)
                .plannerName(request.plannerName())
                .title(request.title())
                .description(request.description())
                .location(request.location())
                .career(request.career())
                .partnerCompany(request.partnerCompany())
                .totalPrice(totalPrice) // 필요한 가격 전체 총합 계산
                .contractCount(0L)
                .avgPrice(0L)
                .minPrice(0L)
                .maxPrice(0L)
                .build();
    }

    public static List<PriceItem> getPriceItem(List<PortfolioRequest.ItemDTO> items, Portfolio portfolio){
        return items.stream()
                .map(item -> PriceItem.builder()
                        .portfolio(portfolio)
                        .itemTitle(item.itemTitle())
                        .itemPrice(item.itemPrice())
                        .build())
                .toList();
    }

    public static PortfolioResponse.FindByIdDTO FindByIdDTOConvertor(Portfolio portfolio,
                                                            List<String> images, List<PriceItem> priceItems,
                                                            List<Match> matches, List<Quotation> quotations,
                                                            Boolean isLiked, Boolean isPremium) {
        // 가격 항목 DTO 변환
        List<PortfolioResponse.PriceItemDTO> priceItemDTOS = PriceItemDTOConvertor(priceItems);
        Long totalPrice = PriceCalculator.calculatePortfolioPrice(priceItemDTOS);
        PortfolioResponse.PriceDTO priceDTO = new PortfolioResponse.PriceDTO(totalPrice, priceItemDTOS);

        // 거래 내역
        // 일반 회원의 경우 거래내역으로 null 반환
        PortfolioResponse.PaymentHistoryDTO paymentHistoryDTO = null;

        // 프리미엄 회원일 경우 이전 거래 내역 History 반환
        if (isPremium) {
            List<PortfolioResponse.PaymentDTO> paymentDTOS = PaymentDTOConvertor(matches, quotations);
            paymentHistoryDTO =
                    new PortfolioResponse.PaymentHistoryDTO(
                    portfolio.getAvgPrice(),
                    portfolio.getMinPrice(),
                    portfolio.getMaxPrice(),
                    paymentDTOS
            );
        }

        return FindByIdDTOConvertor(portfolio, images, priceDTO, paymentHistoryDTO, isLiked);
    }

    private static PortfolioResponse.FindByIdDTO FindByIdDTOConvertor(Portfolio portfolio, List<String> images,
                                                             PortfolioResponse.PriceDTO priceDTO,
                                                             PortfolioResponse.PaymentHistoryDTO paymentHistoryDTO,
                                                             Boolean isLiked) {
        return new PortfolioResponse.FindByIdDTO(
                portfolio.getId(),
                portfolio.getPlanner().getId(),
                images,
                portfolio.getTitle(),
                portfolio.getPlannerName(),
                portfolio.getContractCount(),
                priceDTO,
                portfolio.getLocation(),
                portfolio.getDescription(),
                portfolio.getCareer(),
                portfolio.getPartnerCompany(),
                paymentHistoryDTO,
                isLiked
        );
    }

    public static List<PortfolioResponse.FindAllDTO> FindAllDTOConvertor(List<Portfolio> portfolios, List<String> images, List<Favorite> favorites) {
        return IntStream
                .range(0, portfolios.size())
                .mapToObj(i -> {
                    Portfolio portfolio = portfolios.get(i);
                    return new PortfolioResponse.FindAllDTO(
                            portfolio.getId(),
                            images.get(i),
                            portfolio.getTitle(),
                            portfolio.getPlannerName(),
                            portfolio.getTotalPrice(),
                            portfolio.getLocation(),
                            portfolio.getContractCount(),
                            favorites.stream().anyMatch(favorite -> Objects.equals(favorite.getPortfolio().getId(), portfolio.getId()))
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
                                                                           List<String> images,
                                                                           List<PriceItem> priceItems) {
        return new PortfolioResponse.MyPortfolioDTO(
                portfolio.getPlannerName(),
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
