package com.kakao.sunsuwedding.review.image;

import com.kakao.sunsuwedding.review.Review;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reviewimageitem_tb")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewImageItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Review review;

    @Column(name = "origin_file_name", nullable = false)
    private String originFileName;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Column(nullable = false)
    private boolean thumbnail;

    @Builder
    public ReviewImageItem(Long id, Review review, String originFileName, String filePath, Long fileSize, boolean thumbnail) {
        this.id = id;
        this.review = review;
        this.originFileName = originFileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.thumbnail = thumbnail;
    }
}

