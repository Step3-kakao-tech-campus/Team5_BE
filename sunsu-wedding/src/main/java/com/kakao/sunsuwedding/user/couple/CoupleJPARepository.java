package com.kakao.sunsuwedding.user.couple;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoupleJPARepository extends JpaRepository<Couple, Integer> {
    Optional<Couple> findByEmail(String email);
}
