package com.kakao.sunsuwedding.portfolio.image;

import com.kakao.sunsuwedding.portfolio.Portfolio;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageItemRepository extends JpaRepository<ImageItem, Long> {
    @EntityGraph("ImageItemWithPortfolio")
    List<ImageItem> findAllByThumbnailAndPortfolioIn(boolean thumbnail, List<Portfolio> portfolio);

    @EntityGraph("ImageItemWithPortfolioAndPlanner")
    List<ImageItem> findByPortfolioId(Long id);

    void deleteAllByPortfolioPlannerId(int id);
}
