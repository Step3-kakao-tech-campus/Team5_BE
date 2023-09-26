package com.kakao.sunsuwedding.portfolio.price;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriceItemRepository extends JpaRepository<PriceItem, Long> {
    @EntityGraph("PriceItemWithPortfolioAndPlanner")
    List<PriceItem> findAllByPortfolioId(Long id);

    void deleteAllByPortfolioPlannerId(int id);
}
