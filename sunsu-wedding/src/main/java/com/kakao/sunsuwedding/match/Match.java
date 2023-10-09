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

    @Column
    private LocalDateTime confirmed_at;

    @Column(nullable = false)
    private LocalDateTime created_at;

    @Column(nullable = false)
    private Boolean is_active;

    @Builder
    public Match(Long id, Planner planner, Couple couple, Long price) {
        this.id = id;
        this.planner = planner;
        this.couple = couple;
        this.status = MatchStatus.UNCONFIRMED;
        this.price = price;
        this.created_at = LocalDateTime.now();
        this.is_active = true;
    }

    public void updateStatus(MatchStatus status) {
        this.status = status;
    }

    public void updatePrice(Long price) {
        this.price = price;
    }

    public void updateConfirmedAt(LocalDateTime confirmed_at) {
        this.confirmed_at = confirmed_at;
    }

    public void updateIsActive(Boolean is_active) {
        this.is_active = is_active;
    }
}
