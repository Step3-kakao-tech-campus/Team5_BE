package com.kakao.sunsuwedding.user;

import com.kakao.sunsuwedding._core.DummyEntity;
import com.kakao.sunsuwedding.user.base_user.User;
import com.kakao.sunsuwedding.user.base_user.UserJPARepository;
import com.kakao.sunsuwedding.user.couple.Couple;
import com.kakao.sunsuwedding.user.couple.CoupleJPARepository;
import com.kakao.sunsuwedding.user.planner.Planner;
import com.kakao.sunsuwedding.user.token.Token;
import com.kakao.sunsuwedding.user.token.TokenJPARepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureDataJpa
@DataJpaTest
public class TokenJPARepositoryTest extends DummyEntity {

    @Autowired
    private TokenJPARepository tokenJPARepository;

    @Autowired
    private UserJPARepository userJPARepository;

    @Autowired
    private EntityManager em;

    private Long id;
    private Long userId;

    @BeforeEach
    public void setUp(){
        User user = userJPARepository.save(newPlanner("asdf"));
        userId = user.getId();
        id = tokenJPARepository.save(newToken(user)).getId();
        em.clear();
    }

    @DisplayName("사용자 id로 토큰 찾기 - 성공")
    @Test
    public void findByUserId_success_test() {
        // when
        Token token = tokenJPARepository.findByUserId(userId).orElseThrow(
                () -> new RuntimeException("해당 사용자의 토큰을 찾을 수 없습니다.")
        );

        // then (상태 검사)
        assertThat(token.getAccessToken()).isEqualTo("accessToken");
        assertThat(token.getRefreshToken()).isEqualTo("refreshToken");
    }

    @DisplayName("토큰 저장하기 - 성공")
    @Test
    public void saveToken_success_test() {
        // when
        Couple couple = newCouple("qwer");
        Token token = tokenJPARepository.save(newToken(couple));

        // then (상태 검사)
        assertThat(token.getId()).isEqualTo(2);
        assertThat(token.getAccessToken()).isEqualTo("accessToken");
        assertThat(token.getRefreshToken()).isEqualTo("refreshToken");
    }
    @DisplayName("토큰 삭제하기- 성공")
    @Test
    public void deleteToken_success_test() {
        // when
        long previous_counts = tokenJPARepository.count();
        Token token = tokenJPARepository.findById(id).orElseThrow(
                () -> new RuntimeException("해당 사용자의 토큰을 찾을 수 없습니다.")
        );
        tokenJPARepository.delete(token);

        // then (상태 검사)
        assertThat(tokenJPARepository.count()).isEqualTo(previous_counts-1);
    }
}
