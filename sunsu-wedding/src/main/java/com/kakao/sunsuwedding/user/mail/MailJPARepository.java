package com.kakao.sunsuwedding.user.mail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailJPARepository extends JpaRepository<Mail, Long> {
}
