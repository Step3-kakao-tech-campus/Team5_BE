package com.kakao.sunsuwedding._core;

import com.kakao.sunsuwedding.user.constant.Grade;
import com.kakao.sunsuwedding.user.couple.Couple;
import com.kakao.sunsuwedding.user.planner.Planner;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class DummyEntity {
    protected Couple newCouple(String username){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return Couple.builder()
                .email(username+"@nate.com")
                .password(passwordEncoder.encode("couple1234!"))
                .username("couple")
                .grade(Grade.NORMAL)
                .build();
    }
    protected Planner newPlanner(String username){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return Planner.builder()
                .email(username+"@nate.com")
                .password(passwordEncoder.encode("planner1234!"))
                .username("planner")
                .grade(Grade.NORMAL)
                .build();
    }
}
