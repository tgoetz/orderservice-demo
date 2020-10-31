package com.roche.orderservice.repository;

import com.roche.orderservice.model.Order;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link Specification} for querying orders by time period
 */
public class OrderSpecification implements Specification<Order> {

    private final LocalDateTime from;
    private final LocalDateTime to;

    public OrderSpecification(LocalDateTime from, LocalDateTime to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        Path<LocalDateTime> createdAt = root.get("createdAt");

        if (from != null) {
            predicates.add(cb.greaterThanOrEqualTo(createdAt, from));
        }
        if (to != null) {
            predicates.add(cb.lessThanOrEqualTo(createdAt, to));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }

}
