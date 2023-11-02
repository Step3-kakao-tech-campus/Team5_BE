package com.kakao.sunsuwedding.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewJPARepository extends JpaRepository<Review, Long> {
    List<Review> findAllByMatchCoupleId(@Param("coupleId")Long coupleId);
    Page<Review> findAllByMatchPlannerId(Long plannerId, Pageable pageable);
}
