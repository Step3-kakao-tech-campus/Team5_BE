package com.kakao.sunsuwedding._core.security;

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

        if (plannerOP.isPresent()) {
            Planner plannerPS = plannerOP.get();
            return new CustomPlannerDetails(plannerPS);
        }
        else if (coupleOP.isPresent()) {
            Couple couplePS = coupleOP.get();
            return new CustomCoupleDetails(couplePS);
        }
        else return null;
    }
}
