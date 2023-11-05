package com.kakao.sunsuwedding.user.mail;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Table(name="mail_tb")
@SQLDelete(sql = "UPDATE mail_tb SET is_active = false WHERE id = ?")
@Where(clause = "is_active = true")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "email", nullable = false)
    String eamil;

    @Column(name = "code", nullable = false)
    String code;

    @Column(name = "created_at", nullable = false)
    LocalDateTime createdAt;

    @Column(name = "is_active", nullable = false)
    Boolean isActive;

    @Builder
    public Mail(Long id, String eamil, String code, LocalDateTime createdAt) {
        this.id = id;
        this.eamil = eamil;
        this.code = code;
        this.createdAt = (createdAt == null? LocalDateTime.now() : createdAt);
        this.isActive = true;
    }
}
