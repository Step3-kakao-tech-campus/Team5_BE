package com.kakao.sunsuwedding.review.image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewImageItemJPARepository extends JpaRepository<ReviewImageItem, Long> {

    @Modifying
    @Query("delete from ReviewImageItem r where r.review.id = :reviewId")
    void deleteAllByReviewId(@Param("reviewId") Long reviewId);

    @Query("select ri from ReviewImageItem ri join fetch ri.review r where r.id in :reviewIds")
    List<ReviewImageItem> findAllByReviewIds(@Param("reviewIds") List<Long> reviewIds);

    @Query("select r.image from ReviewImageItem r where r.review.id = :reviewId")
    List<String> findByReviewId(@Param("reviewId") Long reviewId);
}
