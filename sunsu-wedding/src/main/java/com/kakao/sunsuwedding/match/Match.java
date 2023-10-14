package com.kakao.sunsuwedding.match;

import com.kakao.sunsuwedding.user.couple.Couple;
import com.kakao.sunsuwedding.user.planner.Planner;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@SQLDelete(sql = "UPDATE match_tb SET is_active = false WHERE id = ?")
@Where(clause = "is_active = true")
@Table(name="match_tb")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Planner planner;

    @ManyToOne(fetch = FetchType.LAZY)
    private Couple couple;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MatchStatus status;

    @Column(nullable = false)
    private Long price;

    @Column(name = "confirmed_price", nullable = false)
    private Long confirmedPrice;

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false, name = "is_active")
    private Boolean isActive;

    @Builder
    public Match(Long id, Planner planner, Couple couple, Long price, Long confirmedPrice) {
        this.id = id;
        this.planner = planner;
        this.couple = couple;
        this.status = MatchStatus.UNCONFIRMED;
        this.price = price;
        this.confirmedPrice = (confirmedPrice == null? 0 : confirmedPrice);
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
    }

    public void updatePrice(Long price) {
        this.price = price;
    }

    public void updateConfirmedPrice(Long price) {
        this.confirmedPrice = price;
        this.confirmedAt = LocalDateTime.now();
    }

    public void updateStatusConfirmed(Long price) {
        this.status = MatchStatus.CONFIRMED;
        this.confirmedPrice = price;
        this.confirmedAt = LocalDateTime.now();
    }
}
