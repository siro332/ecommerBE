package com.microservices.catalogservice.models.specification;

import com.microservices.catalogservice.models.entities.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

@RequiredArgsConstructor
public class ProductSpecification implements Specification<Product> {

    private SearchCriteria criteria;

    public ProductSpecification(final SearchCriteria criteria) {
        super();
        this.criteria = criteria;
    }

    public SearchCriteria getCriteria() {
        return criteria;
    }

    @Override
    public Specification<Product> and(Specification<Product> other) {
        return Specification.super.and(other);
    }

    @Override
    public Specification<Product> or(Specification<Product> other) {
        return Specification.super.or(other);
    }

    @Override
    public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Expression<String> path;
        if (criteria.getKey().contains(".")) {
            String[] paths = criteria.getKey().split("\\.");
            Path<Object> temp = root.get(paths[0]);
            if (paths.length > 2) {
                for (int i = 1; i < paths.length - 1; i++) {
                    temp = temp.get(paths[i]);
                }
            }
            path = temp.get(paths[paths.length - 1]);
        } else {
            path = root.get(criteria.getKey());
        }
        switch (criteria.getOperation()) {
            case EQUALITY:
                return builder.equal(path, criteria.getValue());
            case NEGATION:
                return builder.notEqual(path, criteria.getValue());
            case GREATER_THAN:
                return builder.greaterThan(path, criteria.getValue().toString());
            case LESS_THAN:
                return builder.lessThan(path, criteria.getValue().toString());
            case LIKE:
                return builder.like(path, criteria.getValue().toString());
            case STARTS_WITH:
                return builder.like(path, criteria.getValue() + "%");
            case ENDS_WITH:
                return builder.like(path, "%" + criteria.getValue());
            case CONTAINS:
                return builder.like(path, "%" + criteria.getValue() + "%");
            default:
                return null;
        }
    }
}
