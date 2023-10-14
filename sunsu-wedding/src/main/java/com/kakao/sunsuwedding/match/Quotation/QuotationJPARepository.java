package com.kakao.sunsuwedding.match.Quotation;

import com.kakao.sunsuwedding.match.Match;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuotationJPARepository extends JpaRepository<Quotation, Long> {
    @EntityGraph("QuotationWithMatch")
    List<Quotation> findAllByMatch(Match match);

    @Query("select q from Quotation q where q.match.id in :matchIds")
    List<Quotation> findAllByMatchIds(@Param("matchIds") List<Long> MatchIds);
}
