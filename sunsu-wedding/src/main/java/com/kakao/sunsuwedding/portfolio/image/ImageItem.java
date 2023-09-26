package com.kakao.sunsuwedding.portfolio.image;

import com.kakao.sunsuwedding.portfolio.Portfolio;
import jakarta.persistence.*;

@Entity
public class ImageItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Portfolio portfolio;

    private String originFileName;
    private String filePath;
    private Long fileSize;


}
