package com.kakao.sunsuwedding._core;

import com.kakao.sunsuwedding.favorite.Favorite;
import com.kakao.sunsuwedding.match.Match;
import com.kakao.sunsuwedding.match.MatchStatus;
import com.kakao.sunsuwedding.payment.Payment;
import com.kakao.sunsuwedding.portfolio.Portfolio;
import com.kakao.sunsuwedding.portfolio.image.PortfolioImageItem;
import com.kakao.sunsuwedding.portfolio.price.PriceItem;
import com.kakao.sunsuwedding.quotation.Quotation;
import com.kakao.sunsuwedding.quotation.QuotationStatus;
import com.kakao.sunsuwedding.user.base_user.User;
import com.kakao.sunsuwedding.user.couple.Couple;
import com.kakao.sunsuwedding.user.planner.Planner;
import com.kakao.sunsuwedding.user.token.Token;

public class DummyEntity {
    protected Couple newCouple(String username){
        return Couple.builder()
                .email(username+"@nate.com")
                .password("couple1234!")
                .username(username)
                .isActive(true)
                .build();
    }
    protected Planner newPlanner(String username){
        return Planner.builder()
                .email(username+"@nate.com")
                .password("planner1234!")
                .username(username)
                .isActive(true)
                .build();
    }
    protected Planner unActivePlanner(String username){
        return Planner.builder()
                .email(username+"@nate.com")
                .password("planner1234!")
                .username(username)
                .isActive(false)
                .build();
    }
    protected Match newMatch(Couple couple, Planner planner, MatchStatus status, Long price, Long confirmedPrice){
        return Match.builder()
                .couple(couple)
                .planner(planner)
                .price(price)
                .confirmedPrice(confirmedPrice)
                .status(status)
                .build();
    }
    protected Token newToken(User user){
        return Token.builder()
                .user(user)
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();
    }

    protected Payment newPayment(User user, String orderId, String paymentKey, Long payedAmount){
        return Payment.builder()
                .user(user)
                .orderId(orderId)
                .paymentKey(paymentKey)
                .payedAmount(payedAmount)
                .build();
    }

    protected Portfolio newPortfolio(Planner planner){
        return Portfolio.builder()
                .planner(planner)
                .plannerName("plannerName")
                .title("newTitle")
                .description("newDescription")
                .location("newLocation")
                .career("newCareer")
                .partnerCompany("newPartnerCompany")
                .totalPrice(10000L)
                .contractCount(20000L)
                .avgPrice(30000L)
                .minPrice(40000L)
                .maxPrice(60000L)
                .build();
    }

    protected PortfolioImageItem newImageItem(Portfolio portfolio, String image, Boolean isThumbnail){
        return PortfolioImageItem.builder()
                .portfolio(portfolio)
                .image(image)
                .thumbnail(isThumbnail)
                .build();
    }

    protected PriceItem newPriceItem(Portfolio portfolio, String title, Long price){
        return PriceItem.builder()
                .portfolio(portfolio)
                .itemTitle(title)
                .itemPrice(price)
                .build();
    }

    protected Quotation newQuotation(Match match, Long price, QuotationStatus status){
        return Quotation.builder()
                .match(match)
                .title("title")
                .price(price)
                .company("company")
                .description("description")
                .status(status)
                .build();
    }

    protected Favorite newFavorite(User user, Portfolio portfolio) {
        return Favorite.builder()
                .user(user)
                .portfolio(portfolio)
                .build();
    }
}
