package com.kakao.sunsuwedding.portfolio.price;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface PriceItemJPARepository extends JpaRepository<PriceItem, Long> {

    @EntityGraph("PriceItemWithPortfolioAndPlanner")
    List<PriceItem> findAllByPortfolioId(Long id);

    void deleteAllByPortfolioPlannerId(Long id);

    @Modifying
    @Query("delete from PriceItem p where p.portfolio.id = :portfolioId")
    void deleteAllByPortfolioId(@Param("portfolioId") Long portfolioId);
}
