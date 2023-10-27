package com.kakao.sunsuwedding.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewJPARepository extends JpaRepository<Review, Long> {
    public List<Review> findAllByMatchCoupleId(@Param("coupleId")Long coupleId);
    public List<Review> findAllByMatchPlannerId(@Param("plannerId")Long plannerId);


}
