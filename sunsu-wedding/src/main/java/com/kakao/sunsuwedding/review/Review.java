package com.kakao.sunsuwedding.review;

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
@SQLDelete(sql = "UPDATE review_tb SET is_active = false WHERE id = ?")
@Where(clause = "is_active = true")
@Table(name="review_tb")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    Planner planner;

    @ManyToOne(fetch = FetchType.LAZY)
    Couple couple;

    @Column(nullable = false)
    String content;

    @Column(name = "created_at", nullable = false)
    LocalDateTime createdAt;

    @Column(name = "modified_at")
    LocalDateTime modifiedAt;

    @Column(name = "is_active", nullable = false)
    Boolean isActive;

    @Builder
    public Review (Long id, Planner planner, Couple couple, String content, LocalDateTime createdAt) {
        this.id = id;
        this.planner = planner;
        this.couple = couple;
        this.content = content;
        this.createdAt = (createdAt == null? LocalDateTime.now() : createdAt);
        this.isActive = true;
    }

    public void updateContent(String content) {
        this.content = content;
        this.modifiedAt = LocalDateTime.now();
    }
}
