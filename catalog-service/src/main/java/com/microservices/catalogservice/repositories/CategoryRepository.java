package com.microservices.catalogservice.repositories;

import com.microservices.catalogservice.models.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    Optional<Category> findByCodeAndIsActiveIsTrue(String code);
    List<Category> findByIsActiveIsTrue();
    Set<Category> findByCodeInAndIsActiveIsTrue(Set<String> categoryCodeList);
    List<Category> findAllByParentCodeIsNullAndIsActiveIsTrue();
    List<Category> findAllByParentCodeIsNotNullAndIsActiveIsTrue();
    List<Category> findAllByCodeInAndIsActiveIsTrue(List<String> codeList);
}
