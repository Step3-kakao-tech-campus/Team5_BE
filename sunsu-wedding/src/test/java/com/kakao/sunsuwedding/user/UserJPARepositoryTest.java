package com.kakao.sunsuwedding.user;

import com.kakao.sunsuwedding._core.DummyEntity;
import com.kakao.sunsuwedding.user.base_user.User;
import com.kakao.sunsuwedding.user.base_user.UserJPARepository;
import com.kakao.sunsuwedding.user.couple.Couple;
import com.kakao.sunsuwedding.user.couple.CoupleJPARepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserJPARepositoryTest extends DummyEntity {

    @Autowired
    private UserJPARepository userJPARepository;

    @Autowired
    private EntityManager em;

    @BeforeEach
    public void setUp(){
        userJPARepository.save(newCouple("zxcv"));
    }

    @DisplayName("사용자 id로 찾기 - 성공")
    @Test
    public void findById_success_test() {
        // given
        Long userId = 1L;
        // when
        User user = userJPARepository.findById(userId).orElseThrow(
                () -> new RuntimeException("해당 유저를 찾을 수 없습니다.")
        );

        // then (상태 검사)
        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getEmail()).isEqualTo("zxcv@nate.com");
        assertThat(user.getPassword()).isEqualTo("couple1234!");
        assertThat(user.getUsername()).isEqualTo("couple");
        assertThat(user.getGrade().getGradeName()).isEqualTo("normal");
    }

}
