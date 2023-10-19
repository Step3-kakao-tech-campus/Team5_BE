package com.kakao.sunsuwedding.review;

import com.kakao.sunsuwedding.user.couple.Couple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewJPARepository extends JpaRepository<Review, Long> {
    public List<Review> findAllByCoupleId(@Param("coupleId")Long coupleId);
    public List<Review> findAllByPlannerId(@Param("plannerId")Long plannerId);


}
