package com.kakao.sunsuwedding.util;

import com.kakao.sunsuwedding._core.DummyEntity;
import com.kakao.sunsuwedding._core.utils.PriceCalculator;
import com.kakao.sunsuwedding.match.Match;
import com.kakao.sunsuwedding.match.MatchStatus;
import com.kakao.sunsuwedding.portfolio.PortfolioResponse;
import com.kakao.sunsuwedding.quotation.Quotation;
import com.kakao.sunsuwedding.quotation.QuotationStatus;
import com.kakao.sunsuwedding.user.couple.Couple;
import com.kakao.sunsuwedding.user.planner.Planner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PriceCalculatorTest extends DummyEntity {

    Planner planner = newPlanner("planner");
    Planner planner2 = newPlanner("planner2");
    Couple couple = newCouple("couple");
    Couple couple2 = newCouple("couple2");
    Match match = newMatch(couple, planner, MatchStatus.UNCONFIRMED,3000L, 3000L);

    @DisplayName("포트폴리오 가격 총합 계산 - calculatePortfolioPrice()")
    @Test
    void calculatePortfolioPriceTest(){
        List<PortfolioResponse.PriceItemDTO> priceItems = List.of(
                new PortfolioResponse.PriceItemDTO("price1", 1000L),
                new PortfolioResponse.PriceItemDTO("price2", 2000L),
                new PortfolioResponse.PriceItemDTO("price3", 3000L)
        );

        Long result = PriceCalculator.calculatePortfolioPrice(priceItems);
        assertThat(result).isEqualTo(6000L);
    }

    @DisplayName("견적서 가격 총합 계산 - calculateQuotationPrice()")
    @Test
    void calculateQuotationPriceTest(){
        List<Quotation> quotations = List.of(
                newQuotation(match, 1000L, QuotationStatus.CONFIRMED),
                newQuotation(match, 100L, QuotationStatus.UNCONFIRMED),
                newQuotation(match, 10L, QuotationStatus.CONFIRMED)
        );
        Long result = PriceCalculator.calculateQuotationPrice(quotations);
        assertThat(result).isEqualTo(1110L);
    }

    @DisplayName("확정된 견적서 가격 총합 계산 - calculateConfirmedQuotationPrice()")
    @Test
    void calculateConfirmedQuotationPriceTest(){
        List<Quotation> quotations = List.of(
                newQuotation(match, 1000L, QuotationStatus.CONFIRMED),
                newQuotation(match, 200L, QuotationStatus.CONFIRMED),
                newQuotation(match, 30L, QuotationStatus.UNCONFIRMED)
        );
        Long result = PriceCalculator.calculateConfirmedQuotationPrice(quotations);
        assertThat(result).isEqualTo(1200L);
    }

    @DisplayName("플래너의 계약 건수 계산 - getContractCount()")
    @Test
    void getContractCountTest(){
        List<Match> matchList = List.of(
                newMatch(couple, planner, MatchStatus.UNCONFIRMED, 1000L, 0L),
                newMatch(couple, planner2, MatchStatus.CONFIRMED, 3000L, 3000L),
                newMatch(couple2, planner2, MatchStatus.CONFIRMED, 2000L, 0L)
        );
        Long result = PriceCalculator.getContractCount(matchList);
        assertThat(result).isEqualTo(2L);
    }

    @DisplayName("매칭 평균 가격 계산 - calculateAvgPrice()")
    @Test
    void calculateAvgPriceTest(){
        List<Match> matchList = List.of(
                newMatch(couple, planner, MatchStatus.CONFIRMED, 1000L, 1000L),
                newMatch(couple2, planner, MatchStatus.UNCONFIRMED, 2000L, 2000L),
                newMatch(couple, planner2, MatchStatus.CONFIRMED, 3000L, 3000L),
                newMatch(couple2, planner2, MatchStatus.UNCONFIRMED, 4000L, 4000L)
        );
        Long contractCount = PriceCalculator.getContractCount(matchList);
        Long result = PriceCalculator.calculateAvgPrice(matchList, contractCount);

        assertThat(result).isEqualTo(2000L);
    }

    @DisplayName("매칭 최소 가격 계산 - calculateMinPrice()")
    @Test
    void calculateMinPriceTest(){
        List<Match> matchList = List.of(
                newMatch(couple, planner, MatchStatus.CONFIRMED, 1000L, 1000L),
                newMatch(couple2, planner, MatchStatus.UNCONFIRMED, 2000L, 2000L),
                newMatch(couple, planner2, MatchStatus.CONFIRMED, 3000L, 3000L),
                newMatch(couple2, planner2, MatchStatus.UNCONFIRMED, 4000L, 4000L)
        );
        Long result = PriceCalculator.calculateMinPrice(matchList);
        assertThat(result).isEqualTo(1000L);
    }

    @DisplayName("매칭 최대 가격 계산 - calculateMinPrice()")
    @Test
    void calculateMaxPriceTest(){
        List<Match> matchList = List.of(
                newMatch(couple, planner, MatchStatus.CONFIRMED, 1000L, 1000L),
                newMatch(couple2, planner, MatchStatus.UNCONFIRMED, 2000L, 2000L),
                newMatch(couple, planner2, MatchStatus.CONFIRMED, 3000L, 3000L),
                newMatch(couple2, planner2, MatchStatus.UNCONFIRMED, 4000L, 4000L)
        );
        Long result = PriceCalculator.calculateMaxPrice(matchList);
        assertThat(result).isEqualTo(3000L);
    }

}
