package com.kakao.sunsuwedding.portfolio.image;

import com.kakao.sunsuwedding.portfolio.Portfolio;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ImageItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Portfolio portfolio;
    private String originFileName;
    private String filePath;
    private Long fileSize;

    @Builder
    public ImageItem(Long id, Portfolio portfolio, String originFileName, String filePath, Long fileSize) {
        this.id = id;
        this.portfolio = portfolio;
        this.originFileName = originFileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }

    public void updateOriginFileName(String originFileName) {
        this.originFileName = originFileName;
    }
    public void updateFilePath(String filePath) {
        this.filePath = filePath;
    }
    public void updateFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
}
