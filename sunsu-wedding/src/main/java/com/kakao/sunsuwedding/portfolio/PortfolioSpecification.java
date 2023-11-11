package com.kakao.sunsuwedding.portfolio;

import com.kakao.sunsuwedding.portfolio.cursor.CursorRequest;
import com.kakao.sunsuwedding.user.planner.Planner;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PortfolioSpecification {
    public Specification<Portfolio> findPortfolio(CursorRequest request) {
        return ((root, query, criteriaBuilder) -> {
            Fetch<Portfolio, Planner> fetch = null;
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                fetch = root.fetch("planner");
            }

            // 무조건 planner를 join 해서 is_active 여부 확인
            Join<Portfolio, Planner> join = (Join<Portfolio, Planner>) fetch;

            // 조건절을 담을 배열
            List<Predicate> predicates = new ArrayList<>();

            // 커서 조건 추가
            insertCursorConstraint(request, root, criteriaBuilder, predicates);

            // 플래너 이름 조건 추가
            insertNameConstraint(request, criteriaBuilder, join, predicates);

            // 지역 조건 추가
            insertLocationConstraint(request, root, criteriaBuilder, predicates);

            // 최소 가격 조건 추가
            insertMinPriceConstraint(request, root, criteriaBuilder, predicates);

            // 최대 가격 조건 추가
            insertMaxPriceConstraint(request, root, criteriaBuilder, predicates);

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

    private static void insertMaxPriceConstraint(CursorRequest request, Root<Portfolio> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
        if (!valid(request.maxPrice()))
            return;

        predicates.add(
                criteriaBuilder.lessThanOrEqualTo(root.get("totalPrice"), request.maxPrice())
        );
    }

    private static void insertMinPriceConstraint(CursorRequest request, Root<Portfolio> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
        if (!valid(request.minPrice()))
            return;

        predicates.add(
                criteriaBuilder.greaterThanOrEqualTo(root.get("totalPrice"), request.minPrice())
        );
    }

    private static void insertLocationConstraint(CursorRequest request, Root<Portfolio> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
        if (!valid(request.location()))
            return;

        predicates.add(
                criteriaBuilder.equal(root.get("location"), request.location())
        );
    }

    private static void insertNameConstraint(CursorRequest request, CriteriaBuilder criteriaBuilder, Join<Portfolio, Planner> join, List<Predicate> predicates) {
        if (!valid(request.name()))
            return;

        predicates.add(
                criteriaBuilder.equal(join.get("username"), request.name())
        );
    }

    private static void insertCursorConstraint(CursorRequest request, Root<Portfolio> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
        if (request.key().equals(CursorRequest.START_KEY))
            return;

        predicates.add(criteriaBuilder.lessThan(root.get("id"), request.key()));
    }

    private static boolean valid(String data) {
        return data != null && !data.equals("null") && !data.equals("");
    }

    private static boolean valid(Long data) {
        return data != null && data >= 0;
    }
}
