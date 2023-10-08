package com.kakao.sunsuwedding.user;

import com.kakao.sunsuwedding._core.DummyEntity;
import com.kakao.sunsuwedding.user.couple.Couple;
import com.kakao.sunsuwedding.user.couple.CoupleJPARepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureDataJpa
@DataJpaTest
public class CoupleJPARepositoryTest extends DummyEntity {

    @Autowired
    private CoupleJPARepository coupleJPARepository;

    @Autowired
    private EntityManager em;

    @BeforeEach
    public void setUp(){
        coupleJPARepository.save(newCouple("ssar"));
        em.clear();
    }

    @AfterEach
    void afterEach() {
        em.createNativeQuery("ALTER TABLE user_tb ALTER COLUMN `id` RESTART WITH 1")
                .executeUpdate();
    }

    @DisplayName("사용자 id로 찾기 - 성공")
    @Test
    public void findById_success_test() {
        // given
        Long userId = 1L;

        // when
        Couple couple = coupleJPARepository.findById(userId).orElseThrow(
                () -> new RuntimeException("해당 사용자를 찾을 수 없습니다.")
        );

        // then (상태 검사)
        assertThat(couple.getId()).isEqualTo(1);
        assertThat(couple.getEmail()).isEqualTo("ssar@nate.com");
        assertThat(couple.getPassword()).isEqualTo("couple1234!");
        assertThat(couple.getUsername()).isEqualTo("couple");
        assertThat(couple.getGrade().getGradeName()).isEqualTo("normal");
    }

}
