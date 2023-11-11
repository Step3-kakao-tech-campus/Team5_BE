package com.kakao.sunsuwedding.portfolio.price;

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
public class PriceItemJPARepositoryTest extends DummyEntity {

    @Autowired
    private PriceItemJPARepository priceItemJPARepository;

    @Autowired
    private PortfolioJPARepository portfolioJPARepository;

    @Autowired
    private PlannerJPARepository plannerJPARepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {

        Planner planner = plannerJPARepository.save(newPlanner("imagePlanner"));
        Portfolio portfolio = portfolioJPARepository.save(newPortfolio(planner));

        Planner planner2 = plannerJPARepository.save(newPlanner("imagePlanner2"));
        Portfolio portfolio2 = portfolioJPARepository.save(newPortfolio(planner2));

        List<PriceItem> priceItemList = List.of(
                newPriceItem(portfolio, "price1", 10000L),
                newPriceItem(portfolio, "price2", 20000L),
                newPriceItem(portfolio, "price3", 30000L),
                newPriceItem(portfolio2, "price4", 40000L),
                newPriceItem(portfolio2, "price5", 50000L),
                newPriceItem(portfolio2, "price6", 60000L),
                newPriceItem(portfolio2, "price7", 70000L)
        );

        priceItemJPARepository.saveAll(priceItemList);
        entityManager.flush();
        entityManager.clear();
    }

    @AfterEach
    void afterEach() {
        entityManager.createNativeQuery("ALTER TABLE price_item_tb ALTER COLUMN `id` RESTART WITH 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE portfolio_tb ALTER COLUMN `id` RESTART WITH 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE user_tb ALTER COLUMN `id` RESTART WITH 1").executeUpdate();
    }

    @DisplayName("포트폴리오 ID로 가격 아이템 찾기 - findAllByPortfolioId()")
    @Test
    void findAllByPortfolioIdTest(){
        // given
        Long portfolioId = 1L;

        // when
        List<PriceItem> priceItems = priceItemJPARepository.findAllByPortfolioId(portfolioId);

        // then
        assertThat(priceItems.size()).isEqualTo(3);

        assertThat(priceItems.get(0).getId()).isEqualTo(1L);
        assertThat(priceItems.get(0).getItemPrice()).isEqualTo(10000L);
        assertThat(priceItems.get(0).getItemTitle()).isEqualTo("price1");

        assertThat(priceItems.get(1).getId()).isEqualTo(2L);
        assertThat(priceItems.get(1).getItemPrice()).isEqualTo(20000L);
        assertThat(priceItems.get(1).getItemTitle()).isEqualTo("price2");
    }

    @DisplayName("플래너 ID로 가격 아이템 삭제하기 - deleteAllByPortfolioPlannerId()")
    @Test
    void deleteAllByPortfolioPlannerIdTest(){
        // given
        Long plannerId = 2L;
        Long previousCount = priceItemJPARepository.count();

        // when
        priceItemJPARepository.deleteAllByPortfolioPlannerId(plannerId);

        // then
        assertThat(previousCount-4).isEqualTo(priceItemJPARepository.count());
    }

    @DisplayName("포트폴리오 ID로 가격 아이템 삭제하기 - deleteAllByPortfolioId()")
    @Test
    void deleteAllByPortfolioIdTest(){
        // given
        Long portfolioId = 1L;
        Long previousCount = priceItemJPARepository.count();

        // when
        priceItemJPARepository.deleteAllByPortfolioPlannerId(portfolioId);

        // then
        assertThat(previousCount-3).isEqualTo(priceItemJPARepository.count());
    }

}
