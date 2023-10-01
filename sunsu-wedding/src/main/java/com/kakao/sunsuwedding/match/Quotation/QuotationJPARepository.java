package com.kakao.sunsuwedding.match.Quotation;

import com.kakao.sunsuwedding.match.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuotationJPARepository extends JpaRepository<Quotation, Long> {
    List<Quotation> findAllByMatch(Match match);
}
