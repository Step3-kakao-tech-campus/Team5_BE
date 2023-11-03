package com.kakao.sunsuwedding.favorite;

import com.kakao.sunsuwedding.portfolio.Portfolio;
import com.kakao.sunsuwedding.user.base_user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name="favorite_tb")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    Portfolio portfolio;

    @Column(name = "created_at", nullable = false)
    LocalDateTime createdAt;

    @Builder
    public Favorite(Long id, User user, Portfolio portfolio, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.portfolio = portfolio;
        this.createdAt = LocalDateTime.now();
    }
}
