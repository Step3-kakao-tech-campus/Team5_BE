package com.kakao.sunsuwedding.review;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.NotFoundException;
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
public class ReviewRepositoryTest {

    @Autowired
    ReviewJPARepository reviewJPARepository;

    @Autowired
    EntityManager entityManager;

    @BeforeEach
    public void beforeEach() {
        List<Review> reviews = List.of(
            Review.builder().id(1L).stars(5).content("review1").build(),
            Review.builder().id(2L).stars(5).content("review2").build()
        );
        reviewJPARepository.saveAll(reviews);
    }

    @AfterEach
    public void afterEach() {
        entityManager
                .createNativeQuery("ALTER TABLE review_tb ALTER COLUMN `id` RESTART WITH 1")
                .executeUpdate();
    }

    @DisplayName("리뷰 생성 테스트")
    @Test
    public void create_review_test() {
        // given
        Review review = Review.builder()
                        .stars(5)
                        .content("review3")
                        .build();

        // when
        Review reviewPS = reviewJPARepository.save(review);

        // then
        assertThat(reviewPS.getId()).isEqualTo(3L);
        assertThat(reviewPS.getContent()).isEqualTo("review3");
    }

    @DisplayName("리뷰 조회 테스트")
    @Test
    public void read_review_test() {
        // given
        Long reviewId = 1L;

        // when
        Review review = reviewJPARepository.findById(reviewId).orElseThrow(
                () -> new NotFoundException(BaseException.REVIEW_NOT_FOUND)
        );

        // then
        assertThat(review.getId()).isEqualTo(1L);
        assertThat(review.getContent()).isEqualTo("review1");

    }

    @DisplayName("리뷰 수정 테스트")
    @Test
    public void update_review_test() {
        // given
        Long reviewId = 1L;
        Review review = reviewJPARepository.findById(reviewId).orElseThrow(
                () -> new NotFoundException(BaseException.REVIEW_NOT_FOUND)
        );
        List<String> images = List.of("/wAA", "/wAA");
        ReviewRequest.UpdateDTO request = new ReviewRequest.UpdateDTO(3, "review1 updated", images);

        // when
        review.updateReview(request);
        Review reviewPS = reviewJPARepository.save(review);

        // then
        assertThat(reviewPS.getId()).isEqualTo(1L);
        assertThat(reviewPS.getContent()).isEqualTo("review1 updated");

    }

    @DisplayName("리뷰 삭제 테스트")
    @Test
    public void delete_review_test() {
        // given
        Long reviewId = 2L;
        long previousCount = reviewJPARepository.count();
        Review review = reviewJPARepository.findById(reviewId).orElseThrow(
                () -> new NotFoundException(BaseException.REVIEW_NOT_FOUND)
        );


        // when
        reviewJPARepository.delete(review);
        long newCount = reviewJPARepository.count();

        // then
        assertThat(newCount).isEqualTo(previousCount - 1);
    }
}
