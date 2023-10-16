package com.kakao.sunsuwedding.portfolio;

import com.kakao.sunsuwedding.portfolio.cursor.CursorRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PortfolioSpecification {
    public static Specification<Portfolio> findPortfolio(Long cursor, Map<String, String> equalKeys, Long minPrice, Long maxPrice) {
        return ((root, query, criteriaBuilder) -> {
            // 조건절을 담을 배열
            List<Predicate> predicates = new ArrayList<>();

            // 시작 커서라면 id 조건은 제외
            if (!cursor.equals(CursorRequest.START_KEY)) {
                predicates.add(criteriaBuilder.lessThan(root.get("id"), cursor));
            }

            // 필터 조건 추가
            equalKeys.keySet().forEach(key ->
                    predicates.add(
                        criteriaBuilder.equal(root.get(key), equalKeys.get(key))
                    )
            );

            if (minPrice != null) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(root.get("totalPrice"), minPrice)
                );
            }

            if (maxPrice != null) {
                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(root.get("totalPrice"), maxPrice)
                );
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
