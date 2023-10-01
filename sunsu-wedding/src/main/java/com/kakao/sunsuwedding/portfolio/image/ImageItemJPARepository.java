package com.kakao.sunsuwedding.portfolio.image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageItemJPARepository extends JpaRepository <ImageItem, Integer> {

    @Modifying
    @Query("delete from ImageItem i where i.portfolio.id = :portfolioId")
    void deleteAllByPortfolioId(@Param("portfolioId") Long portfolioId);
}
