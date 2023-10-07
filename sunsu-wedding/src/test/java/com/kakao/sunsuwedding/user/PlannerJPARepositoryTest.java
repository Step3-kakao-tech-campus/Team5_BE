package com.kakao.sunsuwedding.user;

import com.kakao.sunsuwedding._core.DummyEntity;
import com.kakao.sunsuwedding.user.planner.Planner;
import com.kakao.sunsuwedding.user.planner.PlannerJPARepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

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

    @DisplayName("사용자 id로 찾기 - 성공")
    @Test
    public void findById_success_test() {
        // given
        Long userId = 1L;

        // when
        Planner planner = plannerJPARepository.findById(userId).orElseThrow(
                () -> new RuntimeException("해당 플래너를 찾을 수 없습니다.")
        );

        // then (상태 검사)
        assertThat(planner.getId()).isEqualTo(1);
        assertThat(planner.getEmail()).isEqualTo("ssar@nate.com");
        assertThat(planner.getPassword()).isEqualTo("planner1234!");
        assertThat(planner.getUsername()).isEqualTo("planner");
        assertThat(planner.getGrade().getGradeName()).isEqualTo("normal");
    }

}
