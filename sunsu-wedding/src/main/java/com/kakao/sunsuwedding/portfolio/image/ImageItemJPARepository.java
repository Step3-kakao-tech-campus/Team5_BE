package com.kakao.sunsuwedding.portfolio.image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageItemJPARepository extends JpaRepository <ImageItem, Integer> {
}
