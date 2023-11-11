package com.kakao.sunsuwedding.portfolio;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.sound.sampled.Port;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PortfolioRepositoryTest {
    private final PortfolioJPARepository portfolioJPARepository;
    private final EntityManager entityManager;

    public PortfolioRepositoryTest(@Autowired PortfolioJPARepository portfolioJPARepository,
                                   @Autowired EntityManager entityManager) {
        this.portfolioJPARepository = portfolioJPARepository;
        this.entityManager = entityManager;
    }

    @BeforeEach
    void beforeEach() {
        portfolioJPARepository.save(Portfolio.builder()
                .planner(null)
                .plannerName("1")
                .title("Test Portfolio")
                .description("1")
                .location("Busan")
                .career("1")
                .partnerCompany("1")
                .totalPrice(0L)
                .contractCount(100L)
                .avgPrice(0L)
                .minPrice(0L)
                .maxPrice(0L)
                .build()
        );
        portfolioJPARepository.save(Portfolio.builder()
                .planner(null)
                .plannerName("2")
                .title("Test Portfolio")
                .description("2")
                .location("Busan")
                .career("2")
                .partnerCompany("2")
                .totalPrice(0L)
                .contractCount(100L)
                .avgPrice(0L)
                .minPrice(0L)
                .maxPrice(0L)
                .build()
        );
        entityManager.clear();
    }

    @AfterEach
    public void afterEach() {
        entityManager
                .createNativeQuery("ALTER TABLE portfolio_tb ALTER COLUMN `id` RESTART WITH 1")
                .executeUpdate();
    }

    @DisplayName("Portfolio Repository Test : create")
    @Test
    void repositoryInsertTest() {
        // given
        long previous_count = portfolioJPARepository.count();
        Portfolio portfolio = Portfolio.builder()
                .id(17L)
                .planner(null)
                .plannerName("123")
                .title("Test Portfolio")
                .description("123")
                .location("Busan")
                .career("123")
                .partnerCompany("123")
                .totalPrice(0L)
                .contractCount(100L)
                .avgPrice(0L)
                .minPrice(0L)
                .maxPrice(0L)
                .build();

        // when
        Portfolio portfolioPS = portfolioJPARepository.save(portfolio);

        // then
        assertThat(portfolioJPARepository.count()).isEqualTo(previous_count + 1);
    }

    @DisplayName("Portfolio Repository Test : read")
    @Test
    void repositoryReadTest() {
        // given
        Long portfolioId = 1L;

        // when
        Portfolio portfolio = portfolioJPARepository.findById(portfolioId).orElseThrow();

        // then
        assertThat(portfolio.getId()).isEqualTo(portfolioId);
        assertThat(portfolio.getCareer()).isEqualTo("1");
        assertThat(portfolio.getAvgPrice()).isEqualTo(0);
    }

    @DisplayName("Portfolio Repository Test : update")
    @Test
    void repositoryUpdateTest() {
        // given
        Long portfolioId = 1L;
        Portfolio portfolio = portfolioJPARepository.findById(portfolioId).orElseThrow();

        // when
        portfolio.update(
                "3","3","3","3","3","3",null
        );
        Portfolio portfolioPS = portfolioJPARepository.save(portfolio);

        // then
        assertThat(portfolio.getId()).isEqualTo(portfolioId);
        assertThat(portfolio.getCareer()).isEqualTo("3");
        assertThat(portfolio.getTitle()).isEqualTo("3");
    }

    @DisplayName("Portfolio Repository Test : delete")
    @Test
    void repositoryDeleteTest() {
        // given
        Long portfolioId = 2L;
        long previous_count = portfolioJPARepository.count();

        // when
        portfolioJPARepository.deleteById(portfolioId);

        // then
        assertThat(portfolioJPARepository.count()).isEqualTo(previous_count - 1);
    }
}
