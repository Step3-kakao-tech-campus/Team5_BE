package com.kakao.sunsuwedding.match;

import com.kakao.sunsuwedding.user.couple.Couple;
import com.kakao.sunsuwedding.user.planner.Planner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatchJPARepository extends JpaRepository<Match, Long> {
    List<Match> findAllByPlanner(Planner planner);
    List<Match> findAllByCouple(Couple couple);

    @Query("select m from Match m where m.planner = :planner and m.confirmedAt != null order by m.confirmedAt desc limit 10")
    List<Match> findLatestTenByPlanner(@Param("planner") Planner planner);
}
