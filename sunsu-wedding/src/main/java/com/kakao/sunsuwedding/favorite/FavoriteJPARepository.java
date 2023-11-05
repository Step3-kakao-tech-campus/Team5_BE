package com.kakao.sunsuwedding.favorite;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoriteJPARepository extends JpaRepository<Favorite, Long> {

    @Query("select f from Favorite f where f.user.id = :userId and f.portfolio.id = :portfolioId")
    Optional<Favorite> findByUserAndPortfolio(@Param("userId") Long userId, @Param("portfolioId") Long portfolioId);


    @Query("select f from Favorite f join fetch f.portfolio p " +
            "join fetch p.planner portfolioPlanner " +
            "join fetch f.user u " +
            "where u.id = :userId " +
            "order by f.createdAt desc")
    List<Favorite> findByUserIdFetchJoinPortfolio(@Param("userId") Long userId, Pageable pageable);
}
