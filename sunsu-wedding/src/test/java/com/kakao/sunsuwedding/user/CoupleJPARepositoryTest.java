package com.kakao.sunsuwedding.user;

import com.kakao.sunsuwedding._core.DummyEntity;
import com.kakao.sunsuwedding.user.couple.Couple;
import com.kakao.sunsuwedding.user.couple.CoupleJPARepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
public class CoupleJPARepositoryTest extends DummyEntity {

    @Autowired
    private CoupleJPARepository coupleJPARepository;

    private Long id;

    @BeforeEach
    public void setUp(){
        id = coupleJPARepository.save(newCouple("ssar")).getId();
    }

    @AfterEach
    void tearDown() {
        coupleJPARepository.deleteAll();
    }

    @DisplayName("사용자 id로 찾기 - 성공")
    @Test
    public void findById_success_test() {
        // when
        Couple couple = coupleJPARepository.findById(id).orElseThrow(
                () -> new RuntimeException("해당 사용자를 찾을 수 없습니다.")
        );

        // then (상태 검사)
        assertThat(couple.getEmail()).isEqualTo("ssar@nate.com");
        assertThat(couple.getPassword()).isEqualTo("couple1234!");
        assertThat(couple.getUsername()).isEqualTo("ssar");
        assertThat(couple.getGrade().getGradeName()).isEqualTo("normal");
    }

    @DisplayName("사용자 저장하기")
    @Test
    public void saveCouple_success_test() {
        // when
        Couple c1 = newCouple("newCouple");
        Couple couple = coupleJPARepository.save(c1);

        // then (상태 검사)
        assertThat(couple.getEmail()).isEqualTo("newCouple@nate.com");
        assertThat(couple.getPassword()).isEqualTo("couple1234!");
        assertThat(couple.getUsername()).isEqualTo("newCouple");
        assertThat(couple.getGrade().getGradeName()).isEqualTo("normal");
    }
}
