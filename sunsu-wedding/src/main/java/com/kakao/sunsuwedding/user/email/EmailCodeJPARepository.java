package com.kakao.sunsuwedding.user.email;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailCodeJPARepository extends JpaRepository<EmailCode, Long> {
    Optional<EmailCode> findByEmail(String email);
}
