package com.kakao.sunsuwedding.favorite;

import com.kakao.sunsuwedding._core.DummyEntity;
import com.kakao.sunsuwedding.portfolio.Portfolio;
import com.kakao.sunsuwedding.portfolio.PortfolioJPARepository;
import com.kakao.sunsuwedding.user.couple.Couple;
import com.kakao.sunsuwedding.user.couple.CoupleJPARepository;
import com.kakao.sunsuwedding.user.planner.Planner;
import com.kakao.sunsuwedding.user.planner.PlannerJPARepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class FavoriteRepositoryTest extends DummyEntity {
    @Autowired
    private FavoriteJPARepository favoriteJPARepository;

    @Autowired
    private CoupleJPARepository coupleJPARepository;

    @Autowired
    private PlannerJPARepository plannerJPARepository;

    @Autowired
    private PortfolioJPARepository portfolioJPARepository;

    private Long id1, id2;

    Couple couple;
    Planner planner;
    Portfolio portfolio;

    @BeforeEach
    void setUp() {
        couple = coupleJPARepository.save(newCouple("newcouple"));
        planner = plannerJPARepository.save(newPlanner("newplanner"));
        portfolio = portfolioJPARepository.save(newPortfolio(planner));

        Favorite f1 = newFavorite(couple, portfolio);
        Favorite f2 = newFavorite(planner, portfolio);

        id1 = favoriteJPARepository.save(f1).getId();
        id2 = favoriteJPARepository.save(f2).getId();
    }

    @AfterEach
    void tearDown() {
        favoriteJPARepository.deleteAll();
    }

    @DisplayName("유저와 포트폴리오로 찜하기 조회하기")
    @Test
    void findFavoriteByUserAndPortfolio(){
        // when
        Favorite favorite = favoriteJPARepository.findByUserAndPortfolio(planner, portfolio).orElseThrow(
                () -> new RuntimeException("결제 정보를 찾을 수 없습니다.")
        );

        // then
        assertThat(favorite.getUser().getEmail()).isEqualTo("newplanner@nate.com");
        assertThat(favorite.getPortfolio().getTitle()).isEqualTo("newTitle");
    }
    @DisplayName("찜하기 페이지별로 모아보기")
    @Test
    void findFavoritePageable(){
        // given
        Pageable pageable = Pageable.ofSize(10);
        // when
        List<Favorite> favorite = favoriteJPARepository.findByUserIdFetchJoinPortfolio(planner.getId(), pageable);
        // then
        assertThat(favorite.get(0).getUser().getEmail()).isEqualTo("newplanner@nate.com");
    }
    @DisplayName("찜하기 추가하기")
    @Test
    void saveFavorite(){
        // when
        Couple couple1 = coupleJPARepository.save(newCouple("test_couple"));
        Favorite favorite = favoriteJPARepository.save(newFavorite(couple1, portfolio));

        // then
        assertThat(favorite.getUser().getEmail()).isEqualTo("test_couple@nate.com");
        assertThat(favorite.getPortfolio().getTitle()).isEqualTo("newTitle");
    }
    @DisplayName("찜하기 취소하기")
    @Test
    void deleteFavorite(){
        long previous_counts = favoriteJPARepository.count();
        favoriteJPARepository.deleteById(id1);
        assertThat(favoriteJPARepository.count()).isEqualTo(previous_counts - 1);
    }

}
