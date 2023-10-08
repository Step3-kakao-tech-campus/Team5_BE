package com.kakao.sunsuwedding.user.planner;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlannerJPARepository extends JpaRepository<Planner, Integer> {
    Optional<Planner> findByEmail(String email);
}
