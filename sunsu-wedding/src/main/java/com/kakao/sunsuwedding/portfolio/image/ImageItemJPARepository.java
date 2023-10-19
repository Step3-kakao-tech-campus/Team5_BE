package com.kakao.sunsuwedding.portfolio.image;

import com.kakao.sunsuwedding.portfolio.Portfolio;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ImageItemJPARepository extends JpaRepository<ImageItem, Long> {
    List<ImageItem> findAllByThumbnailAndPortfolioInOrderByPortfolioCreatedAtDesc(boolean thumbnail, List<Portfolio> portfolios);

    @EntityGraph("ImageItemWithPortfolioAndPlanner")
    List<ImageItem> findByPortfolioId(Long id);

    @Modifying
    @Query("delete from ImageItem i where i.portfolio.id = :portfolioId")
    void deleteAllByPortfolioId(@Param("portfolioId") Long portfolioId);

    void deleteAllByPortfolioPlannerId(Long id);
}
