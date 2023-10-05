package com.kakao.sunsuwedding.portfolio.price;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriceItemJPARepository extends JpaRepository<PriceItem, Long> {
    @Query("select p from PriceItem p where p.portfolio.id = :portfolioId")
    List<PriceItem> findByPortfolioId(@Param("portfolioId") Long portfolioId);

    @EntityGraph("PriceItemWithPortfolioAndPlanner")
    List<PriceItem> findAllByPortfolioId(Long id);

    void deleteAllByPortfolioPlannerId(int id);
}