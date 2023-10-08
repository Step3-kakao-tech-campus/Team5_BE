package com.kakao.sunsuwedding._core.security;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.Exception400;
import com.kakao.sunsuwedding.user.couple.Couple;
import com.kakao.sunsuwedding.user.couple.CoupleJPARepository;
import com.kakao.sunsuwedding.user.planner.Planner;
import com.kakao.sunsuwedding.user.planner.PlannerJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final PlannerJPARepository plannerJPARepository;
    private final CoupleJPARepository coupleJPARepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Planner> plannerOP = plannerJPARepository.findByEmail(email);
        Optional<Couple> coupleOP = coupleJPARepository.findByEmail(email);

        Planner plannerPS = plannerOP.orElse(null);
        Couple couplePS = coupleOP.orElse(null);

        // 잘못된 email (플래너도 아니고, 예비 부부도 아님)
        if (plannerPS == null && couplePS == null)
            throw new Exception400(BaseException.USER_NOT_FOUND.getMessage());

        return new CustomUserDetails(plannerPS, couplePS);
    }
}
