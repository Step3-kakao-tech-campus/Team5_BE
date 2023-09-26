package com.kakao.sunsuwedding.portfolio;

import com.kakao.sunsuwedding.user.planner.Planner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    @EntityGraph("PortfolioWithPlanner")
    Page<Portfolio> findAll(Pageable pageable);

    @EntityGraph("PortfolioWithPlanner")
    Optional<Portfolio> findByPlanner(Planner planner);

    void deleteByPlanner(Planner planner);
}
