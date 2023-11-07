package com.kakao.sunsuwedding.portfolio.image;

import com.kakao.sunsuwedding._core.DummyEntity;
import com.kakao.sunsuwedding.portfolio.Portfolio;
import com.kakao.sunsuwedding.portfolio.PortfolioJPARepository;
import com.kakao.sunsuwedding.user.planner.Planner;
import com.kakao.sunsuwedding.user.planner.PlannerJPARepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PortfolioImageItemJPARepositoryTest extends DummyEntity {

    @Autowired
    private PortfolioImageItemJPARepository portfolioImageItemJPARepository;

    @Autowired
    private PortfolioJPARepository portfolioJPARepository;

    @Autowired
    private PlannerJPARepository plannerJPARepository;

    @Autowired
    private EntityManager entityManager;

    Long plannerId;

    @BeforeEach
    void setUp(){

        Planner planner = plannerJPARepository.save(newPlanner("imagePlanner"));
        plannerId = planner.getId();
        Portfolio portfolio = portfolioJPARepository.save(newPortfolio(planner));

        Planner planner2 = plannerJPARepository.save(newPlanner("imagePlanner2"));
        Portfolio portfolio2 = portfolioJPARepository.save(newPortfolio(planner2));

        List<PortfolioImageItem> portfolioImageItemList = List.of(
                newImageItem(portfolio,"/wAA",true),
                newImageItem(portfolio,"/wAA",false),
                newImageItem(portfolio2,"/wAA",true),
                newImageItem(portfolio2,"/wAA",false)
        );
        portfolioImageItemJPARepository.saveAll(portfolioImageItemList);
        entityManager.flush();
        entityManager.clear();
    }

    @AfterEach
    void afterEach() {
        entityManager.createNativeQuery("ALTER TABLE portfolio_image_item_tb ALTER COLUMN `id` RESTART WITH 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE portfolio_tb ALTER COLUMN `id` RESTART WITH 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE user_tb ALTER COLUMN `id` RESTART WITH 1").executeUpdate();
    }

    @DisplayName("포트폴리오 썸네일 이미지 불러오기 - findAllByThumbnailAndPortfolioInOrderByPortfolioCreatedAtDesc()")
    @Test
    void findAllImageItemTest(){

        // when
        List<Portfolio> portfolios = portfolioJPARepository.findAll();
        List<PortfolioImageItem> portfolioImageItemList = portfolioImageItemJPARepository.findAllByThumbnailAndPortfolioInOrderByPortfolioCreatedAtDesc(true, portfolios);

        // then
        assertThat(portfolioImageItemList.size()).isEqualTo(2);

        assertThat(portfolioImageItemList.get(0).getId()).isEqualTo(3);
        assertThat(portfolioImageItemList.get(1).getId()).isEqualTo(1);
    }

    @DisplayName("포트폴리오 ID로 이미지 조회하기 - findByPortfolioId()")
    @Test
    void findByPortfolioIdTest(){
        // when
        Long portfolioId = 1L;

        // given
        List<PortfolioImageItem> portfolioImageItemList = portfolioImageItemJPARepository.findByPortfolioId(portfolioId);

        // then
        assertThat(portfolioImageItemList.size()).isEqualTo(2);
        assertThat(portfolioImageItemList.get(0).getId()).isEqualTo(1);
        assertThat(portfolioImageItemList.get(1).getId()).isEqualTo(2);
    }

    @DisplayName("포트폴리오 ID로 이미지 삭제하기 - deleteAllByPortfolioId()")
    @Test
    void deleteAllByPortfolioIdTest(){
        // when
        Long portfolioId = 1L;
        Long previousCount = portfolioImageItemJPARepository.count();

        // given
        portfolioImageItemJPARepository.deleteAllByPortfolioId(portfolioId);

        // then
        assertThat(previousCount-2).isEqualTo(portfolioImageItemJPARepository.count());
    }

    @DisplayName("플래너 ID로 이미지 삭제하기 - deleteAllByPortfolioPlannerId()")
    @Test
    void deleteAllByPortfolioPlannerIdTest(){
        // when
        Long previousCount = portfolioImageItemJPARepository.count();

        // given
        portfolioImageItemJPARepository.deleteAllByPortfolioPlannerId(plannerId);

        // then
        assertThat(previousCount-2).isEqualTo(portfolioImageItemJPARepository.count());
    }
}
