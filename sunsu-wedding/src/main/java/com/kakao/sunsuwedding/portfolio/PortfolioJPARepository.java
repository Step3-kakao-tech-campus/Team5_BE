package com.kakao.sunsuwedding.portfolio;

import com.kakao.sunsuwedding.user.planner.Planner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PortfolioJPARepository extends JpaRepository<Portfolio, Long> {
    Optional<Portfolio> findByPlanner(Planner planner);
    void deleteByPlanner(Planner planner);
}
