package com.kakao.sunsuwedding._core;

import com.kakao.sunsuwedding.user.constant.Grade;
import com.kakao.sunsuwedding.user.couple.Couple;
import com.kakao.sunsuwedding.user.planner.Planner;
import org.springframework.cglib.core.Local;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

public class DummyEntity {
    protected Couple newCouple(String username){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return Couple.builder()
                .email(username+"@nate.com")
                .password(passwordEncoder.encode("couple1234!"))
                .username("couple")
                .created_at(LocalDateTime.now())
                .is_active(true)
                .grade(Grade.NORMAL)
                .build();
    }
    protected Planner newPlanner(String username){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return Planner.builder()
                .email(username+"@nate.com")
                .password(passwordEncoder.encode("planner1234!"))
                .username("planner")
                .created_at(LocalDateTime.now())
                .is_active(true)
                .grade(Grade.NORMAL)
                .build();
    }
}
