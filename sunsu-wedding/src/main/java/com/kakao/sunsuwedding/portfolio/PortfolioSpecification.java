package com.kakao.sunsuwedding.portfolio;

import com.kakao.sunsuwedding.portfolio.cursor.CursorRequest;
import com.kakao.sunsuwedding.user.planner.Planner;
import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class PortfolioSpecification {
    public static Specification<Portfolio> findPortfolio(CursorRequest request) {
        return ((root, query, criteriaBuilder) -> {
            Fetch<Portfolio, Planner> fetch = null;
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                fetch = root.fetch("planner");
            }

            // 무조건 planner를 join 해서 is_active 여부 확인
            Join<Portfolio, Planner> join = (Join<Portfolio, Planner>) fetch;

            // 조건절을 담을 배열
            List<Predicate> predicates = new ArrayList<>();

            // 시작 커서라면 id 조건은 제외
            if (!request.key().equals(CursorRequest.START_KEY)) {
                predicates.add(criteriaBuilder.lessThan(root.get("id"), request.key()));
            }

            if (valid(request.name())) {
                predicates.add(
                        criteriaBuilder.equal(join.get("username"), request.name())
                );
            }

            if (valid(request.location())) {
                predicates.add(
                        criteriaBuilder.equal(root.get("location"), request.location())
                );
            }

            if (valid(request.minPrice())) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(root.get("totalPrice"), request.minPrice())
                );
            }

            if (valid(request.maxPrice())) {
                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(root.get("totalPrice"), request.maxPrice())
                );
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

    private static boolean valid(String data) {
        return data != null && !data.equals("null") && !data.equals("");
    }

    private static boolean valid(Long data) {
        return data != null;
    }
}
