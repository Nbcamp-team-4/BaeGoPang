package com.team.project.domain.order.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Root;

public final class OrderSortUtils {

    private static final Map<String, String> ALLOWED_SORT_FIELDS = Map.of(
            "createdAt", "createdAt",
            "updatedAt", "updatedAt",
            "orderDate", "orderDate",
            "totalAmount", "totalAmount",
            "status", "status"
    );

    private OrderSortUtils() {
    }

    public static List<jakarta.persistence.criteria.Order> toJpaOrders(
            Sort sort,
            CriteriaBuilder cb,
            Root<com.team.project.domain.order.entity.Order> root
    ) {
        if (sort == null || sort.isUnsorted()) {
            return List.of(cb.desc(root.get("createdAt")));
        }

        return sort.stream()
                .map(s -> {
                    String mappedField = ALLOWED_SORT_FIELDS.getOrDefault(s.getProperty(), "createdAt");
                    return s.isAscending()
                            ? cb.asc(root.get(mappedField))
                            : cb.desc(root.get(mappedField));
                })
                .toList();
    }
}