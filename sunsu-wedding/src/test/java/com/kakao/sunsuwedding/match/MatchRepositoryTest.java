package com.kakao.sunsuwedding.match;

import com.kakao.sunsuwedding._core.DummyEntity;
import com.kakao.sunsuwedding.user.couple.Couple;
import com.kakao.sunsuwedding.user.couple.CoupleJPARepository;
import com.kakao.sunsuwedding.user.planner.Planner;
import com.kakao.sunsuwedding.user.planner.PlannerJPARepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MatchRepositoryTest extends DummyEntity {

    @Autowired
    private MatchJPARepository matchJPARepository;

    @Autowired
    private CoupleJPARepository coupleJPARepository;

    @Autowired
    private PlannerJPARepository plannerJPARepository;

    private Long id1, id2;

    Couple couple;
    Planner planner, planner2;

    @BeforeEach
    void setUp() {
        couple = coupleJPARepository.save(newCouple("newcouple"));
        planner = plannerJPARepository.save(newPlanner("newplanner"));
        planner2 =  plannerJPARepository.save(newPlanner("newplanner2"));

        Match m1 = newMatch(couple, planner, MatchStatus.UNCONFIRMED,0L, 0L);
        Match m2 = newMatch(couple, planner2,MatchStatus.UNCONFIRMED,0L, 0L);

        id1 = matchJPARepository.save(m1).getId();
        id2 = matchJPARepository.save(m2).getId();
    }

    @AfterEach
    void tearDown() {
        matchJPARepository.deleteAll();
        coupleJPARepository.deleteAll();
        plannerJPARepository.deleteAll();
    }

    @Test
    @DisplayName("id로 매칭 찾기")
    void findMatchById(){
        // when
        Match match = matchJPARepository.findById(id1).orElseThrow(
                () -> new RuntimeException("매칭 내역을 찾을 수 없습니다.")
        );

        // then
        assertThat(match.getPlanner().getEmail()).isEqualTo("newplanner@nate.com");
        assertThat(match.getPrice()).isEqualTo(0L);
        assertThat(match.getStatus()).isEqualTo(MatchStatus.UNCONFIRMED);
    }

    @Test
    @DisplayName("플래너로 매칭 찾기")
    void findMatchByPlanner(){

        // when
        List<Match> match = matchJPARepository.findAllByPlanner(planner);

        // then
        assertThat(match.get(0).getCouple().getEmail()).isEqualTo("newcouple@nate.com");
        assertThat(match.get(0).getPlanner().getEmail()).isEqualTo("newplanner@nate.com");
        assertThat(match.get(0).getPrice()).isEqualTo(0L);
        assertThat(match.get(0).getStatus()).isEqualTo(MatchStatus.UNCONFIRMED);
    }

    @Test
    @DisplayName("커플, 플래너로 매칭 찾기")
    void findMatchByCoupleAndPlanner(){
        // when
        List<Match> match = matchJPARepository.findByCoupleAndPlanner(couple, planner);

        // then
        assertThat(match.size()).isEqualTo(1L);
        assertThat(match.get(0).getCouple().getEmail()).isEqualTo("newcouple@nate.com");
        assertThat(match.get(0).getPlanner().getEmail()).isEqualTo("newplanner@nate.com");
        assertThat(match.get(0).getPrice()).isEqualTo(0L);
    }

    @Test
    @DisplayName("플래너 전체확정된 거래내역 10개 들고오기")
    void findLatestTenByPlanner(){
        // when
        List<Match> match = matchJPARepository.findLatestTenByPlanner(planner2);

        // then
        assertThat(match.size()).isEqualTo(0L);
    }

    @Test
    @DisplayName("저장하기")
    void saveMatch(){
        // when
        Couple couple = coupleJPARepository.save(newCouple("test1"));
        Planner planner = plannerJPARepository.save(newPlanner("test2"));
        Match match = matchJPARepository.save(newMatch(couple, planner, MatchStatus.UNCONFIRMED,100L, 0L));

        // then
        assertThat(match.getPlanner().getEmail()).isEqualTo("test2@nate.com");
        assertThat(match.getPrice()).isEqualTo(100L);
        assertThat(match.getStatus()).isEqualTo(MatchStatus.UNCONFIRMED);
    }

    @Test
    @DisplayName("삭제하기")
    void deleteMatch(){
        // when
        long previous_counts = matchJPARepository.count();
        Match match = matchJPARepository.findById(id1).orElseThrow(
                () -> new RuntimeException("존재하지 않는 match id 입니다.")
        );
        matchJPARepository.delete(match);

        // then
        assertThat(matchJPARepository.count()).isEqualTo(previous_counts - 1);
    }

}