package com.kakao.sunsuwedding.portfolio;

import com.kakao.sunsuwedding._core.utils.PriceCalculator;
import com.kakao.sunsuwedding.favorite.Favorite;
import com.kakao.sunsuwedding.match.Match;
import com.kakao.sunsuwedding.portfolio.price.PriceItem;
import com.kakao.sunsuwedding.quotation.Quotation;
import com.kakao.sunsuwedding.user.planner.Planner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

@Component
public class PortfolioDTOConverter {
    private final PriceCalculator priceCalculator;

    public PortfolioDTOConverter(@Autowired PriceCalculator priceCalculator) {
        this.priceCalculator = priceCalculator;
    }

    public Portfolio toPortfolioByRequest(Planner planner, Long totalPrice, PortfolioRequest.AddDTO request){
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

    public List<PriceItem> toPriceItemByPortfolio(List<PortfolioRequest.ItemDTO> items, Portfolio portfolio){
        return items.stream()
                .map(item -> PriceItem.builder()
                        .portfolio(portfolio)
                        .itemTitle(item.itemTitle())
                        .itemPrice(item.itemPrice())
                        .build())
                .toList();
    }

    public PortfolioResponse.FindByIdDTO toFindByIdDTO(Portfolio portfolio,
                                                       List<String> imageItems, List<PriceItem> priceItems,
                                                       List<Match> matches, List<Quotation> quotations,
                                                       Boolean isLiked, Boolean isPremium) {
        // 가격 항목 DTO 변환
        List<PortfolioResponse.PriceItemDTO> priceItemDTOS = toPriceItemDTO(priceItems);
        Long totalPrice = priceCalculator.calculatePortfolioPrice(priceItemDTOS);
        PortfolioResponse.PriceDTO priceDTO = new PortfolioResponse.PriceDTO(totalPrice, priceItemDTOS);

        // 일반 회원의 경우 거래내역으로 null 반환
        if (!isPremium) {
            return toFindByIdDTO(portfolio, imageItems, priceDTO, null, isLiked);
        }

        // 프리미엄 회원일 경우 이전 거래 내역 History 반환
        List<PortfolioResponse.PaymentDTO> paymentDTOS = toPaymentDTO(matches, quotations);
        PortfolioResponse.PaymentHistoryDTO paymentHistoryDTO = new PortfolioResponse.PaymentHistoryDTO(
                portfolio.getAvgPrice(),
                portfolio.getMinPrice(),
                portfolio.getMaxPrice(),
                paymentDTOS
        );

        return toFindByIdDTO(portfolio, imageItems, priceDTO, paymentHistoryDTO, isLiked);
    }

    private PortfolioResponse.FindByIdDTO toFindByIdDTO(Portfolio portfolio, List<String> imageItems,
                                                        PortfolioResponse.PriceDTO priceDTO,
                                                        PortfolioResponse.PaymentHistoryDTO paymentHistoryDTO,
                                                        Boolean isLiked) {
        return new PortfolioResponse.FindByIdDTO(
                portfolio.getId(),
                portfolio.getPlanner().getId(),
                imageItems,
                portfolio.getTitle(),
                portfolio.getPlannerName(),
                portfolio.getContractCount(),
                priceDTO,
                portfolio.getLocation(),
                portfolio.getDescription(),
                portfolio.getCareer(),
                portfolio.getPartnerCompany(),
                portfolio.getAvgStars(),
                paymentHistoryDTO,
                isLiked
        );
    }

    public List<PortfolioResponse.FindAllDTO> toFindAllDTO(List<Portfolio> portfolios, List<String> imageItems, List<Favorite> favorites) {
        return IntStream
                .range(0, portfolios.size())
                .mapToObj(i -> {
                    Portfolio portfolio = portfolios.get(i);
                    return new PortfolioResponse.FindAllDTO(
                            portfolio.getId(),
                            imageItems.get(i),
                            portfolio.getTitle(),
                            portfolio.getPlannerName(),
                            portfolio.getTotalPrice(),
                            portfolio.getLocation(),
                            portfolio.getContractCount(),
                            portfolio.getAvgStars(),
                            favorites.stream().anyMatch(favorite -> Objects.equals(favorite.getPortfolio().getId(), portfolio.getId()))
                    );
                })
                .toList();
    }

    public List<PortfolioResponse.PriceItemDTO> toPriceItemDTO(List<PriceItem> priceItems) {
        return priceItems
                .stream()
                .map(priceItem -> new PortfolioResponse.PriceItemDTO(priceItem.getItemTitle(), priceItem.getItemPrice()))
                .toList();
    }

    public PortfolioResponse.MyPortfolioDTO toMyPortfolioDTO() {
        return new PortfolioResponse.MyPortfolioDTO(
                null,
                List.of(new PortfolioResponse.PriceItemDTO(null, null)),
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    public PortfolioResponse.MyPortfolioDTO toMyPortfolioDTO(Portfolio portfolio,
                                                             List<String> imageItems,
                                                             List<PriceItem> priceItems) {
        return new PortfolioResponse.MyPortfolioDTO(
                portfolio.getPlannerName(),
                toPriceItemDTO(priceItems),
                imageItems,
                portfolio.getTitle(),
                portfolio.getDescription(),
                portfolio.getLocation(),
                portfolio.getCareer(),
                portfolio.getPartnerCompany()
        );
    }

    public List<PortfolioResponse.PaymentDTO> toPaymentDTO(List<Match> matches, List<Quotation> quotations) {
        return matches.stream()
                .map(match -> {
                    List<Quotation> matchingQuotations = quotations.stream()
                            .filter(quotation ->
                                    quotation.getMatch().getId().equals(match.getId()))
                            .toList();
                    return new PortfolioResponse.PaymentDTO(match.getConfirmedPrice(),
                            match.getConfirmedAt().toString().substring(0, 7), // 월까지만 제공
                            toPaymentItemDTO(matchingQuotations));
                })
                .toList();
    }

    public List<PortfolioResponse.PaymentItemDTO> toPaymentItemDTO(List<Quotation> quotations) {
        return quotations
                .stream()
                .map(quotation -> new PortfolioResponse.PaymentItemDTO(quotation.getTitle(), quotation.getPrice(),
                        quotation.getCompany(), quotation.getDescription()))
                .toList();
    }

}
