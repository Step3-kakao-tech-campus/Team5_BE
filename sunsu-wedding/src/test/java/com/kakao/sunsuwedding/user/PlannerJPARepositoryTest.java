package com.kakao.sunsuwedding.user;

import com.kakao.sunsuwedding._core.DummyEntity;
import com.kakao.sunsuwedding.user.planner.Planner;
import com.kakao.sunsuwedding.user.planner.PlannerJPARepository;
import org.junit.jupiter.api.AfterEach;
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

    private Long id;

    @BeforeEach
    public void setUp(){
        id = plannerJPARepository.save(newPlanner("ssar")).getId();
    }

    @AfterEach
    public void teardown(){
        plannerJPARepository.deleteAll();
    }

    @DisplayName("사용자 id로 찾기 - 성공")
    @Test
    public void findById_success_test() {
        // when
        Planner planner = plannerJPARepository.findById(id).orElseThrow(
                () -> new RuntimeException("해당 플래너를 찾을 수 없습니다.")
        );

        // then (상태 검사)
        assertThat(planner.getEmail()).isEqualTo("ssar@nate.com");
        assertThat(planner.getPassword()).isEqualTo("planner1234!");
        assertThat(planner.getUsername()).isEqualTo("ssar");
        assertThat(planner.getGrade().getGradeName()).isEqualTo("normal");
    }

    @DisplayName("사용자 저장하기")
    @Test
    public void savePlanner_success_test() {
        // when
        Planner p1 = newPlanner("newPlanner");
        Planner planner = plannerJPARepository.save(p1);

        // then (상태 검사)
        assertThat(planner.getEmail()).isEqualTo("newPlanner@nate.com");
        assertThat(planner.getPassword()).isEqualTo("planner1234!");
        assertThat(planner.getUsername()).isEqualTo("newPlanner");
        assertThat(planner.getGrade().getGradeName()).isEqualTo("normal");
    }

}
