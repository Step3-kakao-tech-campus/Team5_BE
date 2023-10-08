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
    private final PortfolioJPARepository portfolioJPARepository;
    private final EntityManager entityManager;

    public PortfolioRepositoryTest(@Autowired PortfolioJPARepository portfolioJPARepository,
                                   @Autowired EntityManager entityManager) {
        this.portfolioJPARepository = portfolioJPARepository;
        this.entityManager = entityManager;
    }

    @BeforeEach
    void setUp() {
        portfolioJPARepository.save(Portfolio.builder().title("test1").build());
        portfolioJPARepository.save(Portfolio.builder().title("test2").build());
        entityManager.clear();
    }

    @DisplayName("Portfolio Repository Test : create")
    @Test
    void repositoryInsertTest() {
        // given
        long previous_count = portfolioJPARepository.count();
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
        portfolioJPARepository.save(portfolio);

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
    }

    @DisplayName("Portfolio Repository Test : update")
    @Test
    void repositoryUpdateTest() {
        // given
        Long portfolioId = 1L;
        Portfolio portfolio = portfolioJPARepository.findById(portfolioId).orElseThrow();
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
