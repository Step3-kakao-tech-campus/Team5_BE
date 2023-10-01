package com.kakao.sunsuwedding.user;

import com.kakao.sunsuwedding._core.DummyEntity;
import com.kakao.sunsuwedding.user.couple.Couple;
import com.kakao.sunsuwedding.user.planner.Planner;
import com.kakao.sunsuwedding.user.planner.PlannerJPARepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCrypt;

@DataJpaTest
public class PlannerJPARepositoryTest extends DummyEntity {

    @Autowired
    private PlannerJPARepository plannerJPARepository;

    @Autowired
    private EntityManager em;

    @BeforeEach
    public void setUp(){
        plannerJPARepository.save(newPlanner("ssar"));
    }

    @DisplayName("이메일 찾기 - 성공")
    @Test
    public void findByEmail_success_test() {
        // given
        String email = "ssar@nate.com";

        // when
        Planner plannerPS = plannerJPARepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("해당 이메일을 찾을 수 없습니다.")
        );

        // then (상태 검사)
        Assertions.assertThat(plannerPS.getId()).isEqualTo(1);
        Assertions.assertThat(plannerPS.getEmail()).isEqualTo("ssar@nate.com");
        Assertions.assertThat(BCrypt.checkpw("planner1234!", plannerPS.getPassword())).isEqualTo(true);
        Assertions.assertThat(plannerPS.getUsername()).isEqualTo("planner");
        Assertions.assertThat(plannerPS.getGrade().getGradeName()).isEqualTo("normal");
    }

}
