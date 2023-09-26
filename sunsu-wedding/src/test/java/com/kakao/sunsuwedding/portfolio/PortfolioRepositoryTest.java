package com.kakao.sunsuwedding.portfolio;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PortfolioRepositoryTest {
    private final PortfolioRepository portfolioRepository;
    private final EntityManager entityManager;

    public PortfolioRepositoryTest(@Autowired PortfolioRepository portfolioRepository,
                                   @Autowired EntityManager entityManager) {
        this.portfolioRepository = portfolioRepository;
        this.entityManager = entityManager;
    }

    @BeforeEach
    void setUp() {
        portfolioRepository.save(Portfolio.builder().title("test1").build());
        portfolioRepository.save(Portfolio.builder().title("test2").build());
        entityManager.clear();
    }

    @DisplayName("Portfolio Repository Test : create")
    @Test
    void repositoryInsertTest() {
        // given
        long previous_count = portfolioRepository.count();
        Portfolio portfolio = Portfolio.builder()
                .planner(null)
                .title("Test Portfolio")
                .description("none")
                .location("Busan")
                .career("none")
                .partnerCompany("none")
                .totalPrice(0L)
                .contractCount(100L)
                .avgPrice(0L)
                .minPrice(0L)
                .maxPrice(0L)
                .build();

        // when
        portfolioRepository.save(portfolio);

        // then
        assertThat(portfolioRepository.count()).isEqualTo(previous_count + 1);
    }

    @DisplayName("Portfolio Repository Test : read")
    @Test
    void repositoryReadTest() {
        // given
        Long portfolioId = 1L;

        // when
        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow();

        // then
        assertThat(portfolio.getId()).isEqualTo(portfolioId);
    }

    @DisplayName("Portfolio Repository Test : update")
    @Test
    void repositoryUpdateTest() {
        // given
        Long portfolioId = 1L;
        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow();
    }

    @DisplayName("Portfolio Repository Test : delete")
    @Test
    void repositoryDeleteTest() {
        // given
        Long portfolioId = 2L;
        long previous_count = portfolioRepository.count();

        // when
        portfolioRepository.deleteById(portfolioId);

        // then
        assertThat(portfolioRepository.count()).isEqualTo(previous_count - 1);
    }
}
