package com.kakao.sunsuwedding.user;

import com.kakao.sunsuwedding._core.DummyEntity;
import com.kakao.sunsuwedding.user.base_user.User;
import com.kakao.sunsuwedding.user.base_user.UserJPARepository;
import com.kakao.sunsuwedding.user.couple.Couple;
import org.junit.jupiter.api.AfterEach;
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

    private Long id1, id2, id3;

    @BeforeEach
    public void setUp(){
        id1 = userJPARepository.save(newCouple("asdf")).getId();
        id2 = userJPARepository.save(newPlanner("qwer")).getId();
        id3 = userJPARepository.save(unActivePlanner("zxcv")).getId();
    }

    @AfterEach
    public void teardown(){
        userJPARepository.deleteAll();
    }

    @DisplayName("커플 id로 찾기 - 성공")
    @Test
    public void findCoupleById_success_test() {

        // when
        User user = userJPARepository.findById(id1).orElseThrow(
                () -> new RuntimeException("해당 유저를 찾을 수 없습니다.")
        );

        // then (상태 검사)
        assertThat(user.getEmail()).isEqualTo("asdf@nate.com");
        assertThat(user.getPassword()).isEqualTo("couple1234!");
        assertThat(user.getUsername()).isEqualTo("asdf");
        assertThat(user.getDtype()).isEqualTo("couple");
        assertThat(user.getIsActive()).isEqualTo(true);
        assertThat(user.getGrade().getGradeName()).isEqualTo("normal");
    }
    @DisplayName("플래너 id로 찾기 - 성공")
    @Test
    public void findPlannerById_success_test() {

        // when
        User user = userJPARepository.findById(id2).orElseThrow(
                () -> new RuntimeException("해당 유저를 찾을 수 없습니다.")
        );

        // then (상태 검사)
        assertThat(user.getEmail()).isEqualTo("qwer@nate.com");
        assertThat(user.getPassword()).isEqualTo("planner1234!");
        assertThat(user.getUsername()).isEqualTo("qwer");
        assertThat(user.getDtype()).isEqualTo("planner");
        assertThat(user.getIsActive()).isEqualTo(true);
        assertThat(user.getGrade().getGradeName()).isEqualTo("normal");
    }
    @DisplayName("삭제된 유저 id로 찾기 - 성공")
    @Test
    public void findUnactiveUserById_success_test() {

        // when
        User user = userJPARepository.findByEmailNative("zxcv@nate.com").orElseThrow(
                () -> new RuntimeException("해당 유저를 찾을 수 없습니다.")
        );

        // then (상태 검사)
        assertThat(user.getEmail()).isEqualTo("zxcv@nate.com");
        assertThat(user.getPassword()).isEqualTo("planner1234!");
        assertThat(user.getUsername()).isEqualTo("zxcv");
        assertThat(user.getDtype()).isEqualTo("planner");
        assertThat(user.getIsActive()).isEqualTo(false);
        assertThat(user.getGrade().getGradeName()).isEqualTo("normal");
    }

    @DisplayName("사용자 저장 - 성공")
    @Test
    public void saveUser_success_test() {

        // when
        Couple c1 = newCouple("newCouple");
        User user = userJPARepository.save(c1);

        // then (상태 검사)
        assertThat(user.getEmail()).isEqualTo("newCouple@nate.com");
        assertThat(user.getPassword()).isEqualTo("couple1234!");
        assertThat(user.getUsername()).isEqualTo("newCouple");
        assertThat(user.getGrade().getGradeName()).isEqualTo("normal");
    }
    @DisplayName("사용자 삭제 - 성공")
    @Test
    public void deleteUser_success_test() {

        long previous_counts = userJPARepository.count();

        // when
        userJPARepository.deleteById(id1);

        // then (상태 검사)
        assertThat(userJPARepository.count()).isEqualTo(previous_counts - 1);
    }
}
