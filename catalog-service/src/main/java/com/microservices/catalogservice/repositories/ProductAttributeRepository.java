package com.microservices.catalogservice.repositories;

import com.microservices.catalogservice.models.entities.product_inventory.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductAttributeRepository extends JpaRepository<ProductAttribute, Long> {
    List<ProductAttribute> findAllByIsActiveIsTrue();
    Optional<ProductAttribute> findByCodeAndIsActiveIsTrue(String code);
    Optional<ProductAttribute> findByNameAndIsActiveIsTrue(String name);
}
