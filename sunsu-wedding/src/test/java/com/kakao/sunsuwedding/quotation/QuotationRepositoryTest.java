package com.kakao.sunsuwedding.quotation;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureDataJpa
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DataJpaTest
public class QuotationRepositoryTest {
    private final QuotationJPARepository quotationJPARepository;
    private final EntityManager entityManager;

    public QuotationRepositoryTest(@Autowired QuotationJPARepository quotationJPARepository, @Autowired EntityManager entityManager) {
        this.quotationJPARepository = quotationJPARepository;
        this.entityManager = entityManager;
    }

    @BeforeEach
    void beforeEach() {
        List<Quotation> quotations = List.of(
                Quotation.builder().id(1).title("test title").price(100000L).company("abc").description("asdf").status(QuotationStatus.UNCONFIRMED).build(),
                Quotation.builder().id(2).title("test title2").price(100000L).company("abc2").description("asdf2").status(QuotationStatus.UNCONFIRMED).build()
        );
        quotationJPARepository.saveAll(quotations);
        entityManager.flush();
        entityManager.clear();
    }

    @AfterEach
    void afterEach() {
        entityManager
                .createNativeQuery("ALTER TABLE quotation_tb ALTER COLUMN `id` RESTART WITH 1")
                .executeUpdate();
    }

    @DisplayName("create test")
    @Test
    void createTest() {
        // given
        Quotation quotation = Quotation.builder()
                .title("test title3")
                .price(100000L)
                .company("abc")
                .description("asdf")
                .status(QuotationStatus.UNCONFIRMED)
                .build();

        // when
        Quotation savedQuotation = quotationJPARepository.save(quotation);

        // then
        assertThat(savedQuotation.getTitle()).isEqualTo("test title3");
    }

    @DisplayName("read test")
    @Test
    void readTest() {
        // given
        Long quotationId = 1L;

        // when
        Quotation quotation = quotationJPARepository.findById(quotationId).orElseThrow();

        // then
        assertThat(quotation.getTitle()).isEqualTo("test title");
    }

    @DisplayName("update test")
    @Test
    void updateTest() {
        // given
        Long quotationId = 1L;
        Quotation quotation = quotationJPARepository.findById(quotationId).orElseThrow();
        String updated_title = "update title";
        quotation.updateTitle(updated_title);

        // when
        Quotation updatedQuotation = quotationJPARepository.save(quotation);

        // then
        assertThat(updatedQuotation.getTitle()).isEqualTo(updated_title);
    }

    @DisplayName("delete test")
    @Test
    void deleteTest() {
        // given
        Long quotationId = 1L;
        long previous_counts = quotationJPARepository.count();
        Quotation quotation = quotationJPARepository.findById(quotationId).orElseThrow();

        // when
        quotationJPARepository.delete(quotation);

        // then
        assertThat(quotationJPARepository.count()).isEqualTo(previous_counts - 1);
    }
}
