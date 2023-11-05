package com.kakao.sunsuwedding.user.mail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MailCodeJPARepository extends JpaRepository<MailCode, Long> {
    Optional<MailCode> findByEmail(String email);
}
