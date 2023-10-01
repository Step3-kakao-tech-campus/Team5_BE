package com.kakao.sunsuwedding.portfolio.price;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PriceItemJPARepository extends JpaRepository <PriceItem, Integer> {
    @Query("select p from PriceItem p where p.portfolio.id = :portfolioId")
    List<PriceItem> findByPortfolioId(@Param("portfolioId") Long portfolioId);
}
