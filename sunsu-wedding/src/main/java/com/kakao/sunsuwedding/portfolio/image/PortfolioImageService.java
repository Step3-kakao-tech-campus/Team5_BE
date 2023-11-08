package com.kakao.sunsuwedding.portfolio.image;

import com.kakao.sunsuwedding.portfolio.Portfolio;

import java.util.List;

public interface PortfolioImageService {

    void uploadImage(List<String> imageItems, Portfolio portfolio);

    void updateImage(List<String> imageItems, Portfolio portfolio);
}
