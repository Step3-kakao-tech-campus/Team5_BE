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
public class ImageItemJPARepositoryTest extends DummyEntity {

    @Autowired
    private ImageItemJPARepository imageItemJPARepository;

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

        List<ImageItem> imageItemList = List.of(
                newImageItem(portfolio, "1-1.jpg", "./images/image1.jpg", true),
                newImageItem(portfolio, "1-2.jpg", "./images/image2.jpg", false),
                newImageItem(portfolio2, "1-1.jpg", "./images/image1.jpg", true),
                newImageItem(portfolio2, "1-2.jpg", "./images/image2.jpg", false)
        );
        imageItemJPARepository.saveAll(imageItemList);
        entityManager.flush();
        entityManager.clear();
    }

    @AfterEach
    void afterEach() {
        entityManager.createNativeQuery("ALTER TABLE imageitem_tb ALTER COLUMN `id` RESTART WITH 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE portfolio_tb ALTER COLUMN `id` RESTART WITH 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE user_tb ALTER COLUMN `id` RESTART WITH 1").executeUpdate();
    }

    @DisplayName("포트폴리오 썸네일 이미지 불러오기 - findAllByThumbnailAndPortfolioInOrderByPortfolioCreatedAtDesc()")
    @Test
    void findAllImageItemTest(){

        // when
        List<Portfolio> portfolios = portfolioJPARepository.findAll();
        List<ImageItem> imageItemList = imageItemJPARepository.findAllByThumbnailAndPortfolioInOrderByPortfolioCreatedAtDesc(true, portfolios);

        // then
        assertThat(imageItemList.size()).isEqualTo(2);

        assertThat(imageItemList.get(0).getId()).isEqualTo(3);
        assertThat(imageItemList.get(0).getFilePath()).isEqualTo("./images/image1.jpg");
        assertThat(imageItemList.get(0).getOriginFileName()).isEqualTo("1-1.jpg");

        assertThat(imageItemList.get(1).getId()).isEqualTo(1);
    }

    @DisplayName("포트폴리오 ID로 이미지 조회하기 - findByPortfolioId()")
    @Test
    void findByPortfolioIdTest(){
        // when
        Long portfolioId = 1L;

        // given
        List<ImageItem> imageItemList = imageItemJPARepository.findByPortfolioId(portfolioId);

        // then
        assertThat(imageItemList.size()).isEqualTo(2);
        assertThat(imageItemList.get(0).getId()).isEqualTo(1);
        assertThat(imageItemList.get(0).getFilePath()).isEqualTo("./images/image1.jpg");
        assertThat(imageItemList.get(0).getOriginFileName()).isEqualTo("1-1.jpg");
        assertThat(imageItemList.get(1).getId()).isEqualTo(2);
    }

    @DisplayName("포트폴리오 ID로 이미지 삭제하기 - deleteAllByPortfolioId()")
    @Test
    void deleteAllByPortfolioIdTest(){
        // when
        Long portfolioId = 1L;
        Long previousCount = imageItemJPARepository.count();

        // given
        imageItemJPARepository.deleteAllByPortfolioId(portfolioId);

        // then
        assertThat(previousCount-2).isEqualTo(imageItemJPARepository.count());
    }

    @DisplayName("플래너 ID로 이미지 삭제하기 - deleteAllByPortfolioPlannerId()")
    @Test
    void deleteAllByPortfolioPlannerIdTest(){
        // when
        Long previousCount = imageItemJPARepository.count();

        // given
        imageItemJPARepository.deleteAllByPortfolioPlannerId(plannerId);

        // then
        assertThat(previousCount-2).isEqualTo(imageItemJPARepository.count());
    }
}
