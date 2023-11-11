package com.kakao.sunsuwedding.match;

import com.kakao.sunsuwedding.chat.Chat;
import com.kakao.sunsuwedding.user.couple.Couple;
import com.kakao.sunsuwedding.user.planner.Planner;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;


@Entity
@Table(
        name="match_tb",
        indexes = {
                @Index(name = "match_couple_planner_index", columnList = "planner_id,couple_id"),
                @Index(name = "match_chat_index", columnList = "chat_id")
        })
@SQLDelete(sql = "UPDATE match_tb SET is_active = false WHERE id = ?")
@Where(clause = "is_active = true")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    private Planner planner;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    private Couple couple;

    @OneToOne(fetch = FetchType.LAZY)
    private Chat chat;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MatchStatus status;

    @Column(nullable = false)
    private Long price;

    @Column(name = "confirmed_price", nullable = false)
    private Long confirmedPrice;

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    @Column(name = "review_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ReviewStatus reviewStatus;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false, name = "is_active")
    private Boolean isActive;

    @Builder
    public Match(Long id, Planner planner, Couple couple, Chat chat, MatchStatus status, Long price, Long confirmedPrice) {
        this.id = id;
        this.planner = planner;
        this.couple = couple;
        this.chat = chat;
        this.status = (status == null ? MatchStatus.UNCONFIRMED : status);
        this.price = price;
        this.confirmedPrice = (confirmedPrice == null? 0 : confirmedPrice);
        this.reviewStatus = ReviewStatus.UNWRITTEN;
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
    }

    public void updatePrice(Long price) {
        this.price = price;
    }

    public void updateConfirmedPrice(Long price) {
        this.confirmedPrice = price;
    }

    public void updateStatusConfirmed() {
        this.status = MatchStatus.CONFIRMED;
        this.confirmedAt = LocalDateTime.now();
    }

    public void updateReviewStatus(ReviewStatus reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

}
