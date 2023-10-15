package com.kakao.sunsuwedding.portfolio;

import com.kakao.sunsuwedding.portfolio.cursor.CursorRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PortfolioSpecification {
    public static Specification<Portfolio> findPortfolio(Long cursor, Map<String, String> keys) {
        return ((root, query, criteriaBuilder) -> {
            // 조건절을 담을 배열
            List<Predicate> predicates = new ArrayList<>();

            // 시작 커서라면 id 조건은 제외
            if (!cursor.equals(CursorRequest.START_KEY)) {
                predicates.add(criteriaBuilder.lessThan(root.get("id"), cursor));
            }

            // 필터 조건 추가
            keys.keySet().forEach(key -> {
                predicates.add(
                        criteriaBuilder.equal(root.get(key), keys.get(key))
                );
            });

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
