package com.kakao.sunsuwedding.portfolio.image;

import com.kakao.sunsuwedding.portfolio.Portfolio;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageItemRepository extends JpaRepository<ImageItem, Long> {
    @EntityGraph("ImageItemWithPortfolio")
    @Query("select i from ImageItem i " +
            "where i.thumbnail = true and i.portfolio in :portfolios")
    List<ImageItem> findAllThumbnailsByPortfolio(List<Portfolio> portfolios);

    @EntityGraph("ImageItemWithPortfolio")
    List<ImageItem> findByPortfolioId(Long id);

    @EntityGraph("ImageItemWithPlanner")
    void deleteAllByPortfolioPlannerId(int id);
}
