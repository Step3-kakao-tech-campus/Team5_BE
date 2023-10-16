package com.kakao.sunsuwedding._core;

import com.kakao.sunsuwedding.match.Match;
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
    protected Match newMatch(Couple couple, Planner planner, Long price){
        return Match.builder()
                .couple(couple)
                .planner(planner)
                .price(price)
                .build();
    }
    protected Token newToken(User user){
        return Token.builder()
                .user(user)
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();
    }

}
