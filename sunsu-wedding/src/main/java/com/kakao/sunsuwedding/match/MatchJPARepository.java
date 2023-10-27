package com.kakao.sunsuwedding.match;

import com.kakao.sunsuwedding.user.couple.Couple;
import com.kakao.sunsuwedding.user.planner.Planner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MatchJPARepository extends JpaRepository<Match, Long> {
    List<Match> findAllByPlanner(Planner planner);
    List<Match> findAllByCouple(Couple couple);

    Optional<Match> findByChatId(Long chatId);

    @Query("select m from Match m where m.planner = :planner and m.confirmedAt != null order by m.confirmedAt desc limit 10")
    List<Match> findLatestTenByPlanner(@Param("planner") Planner planner);

    @Query("select m from Match m where m.planner = :planner and m.couple = :couple")
    List<Match> findByCoupleAndPlanner(@Param("couple") Couple couple, @Param("planner") Planner planner);
}
