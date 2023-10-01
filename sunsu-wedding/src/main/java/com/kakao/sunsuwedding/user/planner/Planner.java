package com.kakao.sunsuwedding.user.planner;

import com.kakao.sunsuwedding.user.constant.Grade;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="planner_tb")
public class Planner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(length = 256, nullable = false)
    private String password;

    @Column(length = 45, nullable = false)
    private String username;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime payedAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Grade grade;

    @Builder
    public Planner(int id, String email, String password, String username, LocalDateTime payedAt, Grade grade) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
        this.payedAt = payedAt;
        this.grade = grade;
        this.createdAt = LocalDateTime.now();
    }

    // 유저 등급 업그레이드
    public void upgrade() {
        this.grade = Grade.PREMIUM;
        this.payedAt = LocalDateTime.now();
    }
}