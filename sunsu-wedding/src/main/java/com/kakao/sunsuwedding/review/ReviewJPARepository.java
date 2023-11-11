package com.kakao.sunsuwedding.review;

import com.kakao.sunsuwedding.user.planner.Planner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewJPARepository extends JpaRepository<Review, Long> {
    @Query("select r from Review r join fetch r.match m join fetch m.couple c join fetch m.planner where c.id = :coupleId")
    List<Review> findAllByMatchCoupleId(@Param("coupleId")Long coupleId);

    @Query("select r from Review r join fetch r.match m join fetch m.planner p join fetch m.couple where p.id = :plannerId")
    Page<Review> findAllByMatchPlannerId(@Param("plannerId")Long plannerId, Pageable pageable);

    List<Review> findAllByMatchPlanner(Planner planner);
}
