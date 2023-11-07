package com.kakao.sunsuwedding.user.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface TokenJPARepository extends JpaRepository<Token, Long> {
    Optional<Token> findByUserId(Long userId);
}
