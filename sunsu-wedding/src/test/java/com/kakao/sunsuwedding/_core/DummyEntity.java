package com.kakao.sunsuwedding._core;

import com.kakao.sunsuwedding.match.Match;
import com.kakao.sunsuwedding.user.base_user.User;
import com.kakao.sunsuwedding.user.constant.Grade;
import com.kakao.sunsuwedding.user.couple.Couple;
import com.kakao.sunsuwedding.user.planner.Planner;
import com.kakao.sunsuwedding.user.token.Token;
import org.springframework.cglib.core.Local;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

public class DummyEntity {
    protected Couple newCouple(String username){
        return Couple.builder()
                .email(username+"@nate.com")
                .password("couple1234!")
                .username(username)
                .createdAt(LocalDateTime.now())
                .isActive(true)
                .grade(Grade.NORMAL)
                .build();
    }
    protected Planner newPlanner(String username){
        return Planner.builder()
                .email(username+"@nate.com")
                .password("planner1234!")
                .username(username)
                .createdAt(LocalDateTime.now())
                .isActive(true)
                .grade(Grade.NORMAL)
                .build();
    }
    protected Planner unActivePlanner(String username){
        return Planner.builder()
                .email(username+"@nate.com")
                .password("planner1234!")
                .username(username)
                .createdAt(LocalDateTime.now())
                .isActive(false)
                .grade(Grade.NORMAL)
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
