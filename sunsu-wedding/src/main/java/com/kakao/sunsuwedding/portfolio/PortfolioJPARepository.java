package com.kakao.sunsuwedding.portfolio;

import com.kakao.sunsuwedding.user.planner.Planner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PortfolioJPARepository extends JpaRepository<Portfolio, Long> {
    Page<Portfolio> findAll(Pageable pageable);

    void deleteByPlanner(Planner planner);

    @Query("select p from Portfolio p where p.planner.id = :plannerId")
    Optional<Portfolio> findByPlannerId(@Param("plannerId") Long plannerId);
}
