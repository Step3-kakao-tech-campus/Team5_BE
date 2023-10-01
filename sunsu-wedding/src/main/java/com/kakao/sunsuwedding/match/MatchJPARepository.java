package com.kakao.sunsuwedding.match;

import com.kakao.sunsuwedding.user.couple.Couple;
import com.kakao.sunsuwedding.user.planner.Planner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchJPARepository extends JpaRepository<Match, Long> {
    List<Match> findAllByPlanner(Planner planner);
    List<Match> findAllByCouple(Couple couple);
}
