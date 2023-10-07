package com.kakao.sunsuwedding.portfolio.image;

import com.kakao.sunsuwedding.portfolio.Portfolio;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageItemJPARepository extends JpaRepository<ImageItem, Long> {
    @EntityGraph("ImageItemWithPortfolio")
    List<ImageItem> findAllByThumbnailAndPortfolioIn(boolean thumbnail, List<Portfolio> portfolio);

    @EntityGraph("ImageItemWithPortfolioAndPlanner")
    List<ImageItem> findByPortfolioId(Long id);

    @Modifying
    @Query("delete from ImageItem i where i.portfolio.id = :portfolioId")
    void deleteAllByPortfolioId(@Param("portfolioId") Long portfolioId);

    void deleteAllByPortfolioPlannerId(Long id);
}
