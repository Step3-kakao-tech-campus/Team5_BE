package com.kakao.sunsuwedding.portfolio.image;

import com.kakao.sunsuwedding.portfolio.Portfolio;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PortfolioImageItemJPARepository extends JpaRepository<PortfolioImageItem, Long> {

    List<PortfolioImageItem> findAllByThumbnailAndPortfolioInOrderByPortfolioCreatedAtDesc(Boolean thumbnail, List<Portfolio> portfolios);

    @EntityGraph("ImageItemWithPortfolioAndPlanner")
    List<PortfolioImageItem> findByPortfolioId(Long id);

    @Modifying
    @Query("delete from PortfolioImageItem p where p.portfolio.id = :portfolioId")
    void deleteAllByPortfolioId(@Param("portfolioId") Long portfolioId);

    void deleteAllByPortfolioPlannerId(Long id);

}
