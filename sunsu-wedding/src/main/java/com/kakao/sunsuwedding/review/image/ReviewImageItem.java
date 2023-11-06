package com.kakao.sunsuwedding.review.image;

import com.kakao.sunsuwedding.review.Review;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "review_imageitem_tb")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewImageItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Review review;

    @Lob
    private String image;

    @Column(nullable = false)
    private Boolean thumbnail;

    @Builder
    public ReviewImageItem(Long id, Review review, String image, Boolean thumbnail) {
        this.id = id;
        this.review = review;
        this.image = image;
        this.thumbnail = thumbnail;
    }
}

