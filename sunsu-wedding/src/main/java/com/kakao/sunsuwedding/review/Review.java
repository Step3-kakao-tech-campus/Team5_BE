package com.kakao.sunsuwedding.review;

import com.kakao.sunsuwedding.match.Match;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Table(
        name="review_tb",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"id", "match_id", "is_active"})
        })
@SQLDelete(sql = "UPDATE review_tb SET is_active = false WHERE id = ?")
@Where(clause = "is_active = true")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    Match match;

    @Column(nullable = false)
    Integer stars;

    @Column(nullable = false)
    String content;

    @Column(name = "created_at", nullable = false)
    LocalDateTime createdAt;

    @Column(name = "modified_at")
    LocalDateTime modifiedAt;

    @Column(name = "is_active", nullable = false)
    Boolean isActive;

    @Builder
    public Review (Long id, Match match, Integer stars, String content, LocalDateTime createdAt) {
        this.id = id;
        this.match = match;
        this.stars = stars;
        this.content = content;
        this.createdAt = (createdAt == null? LocalDateTime.now() : createdAt);
        this.isActive = true;
    }

    public void updateReview(ReviewRequest.UpdateDTO request) {
        this.content = request.content();
        this.stars = request.stars();
        this.modifiedAt = LocalDateTime.now();
    }
}
