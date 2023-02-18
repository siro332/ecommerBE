package com.microservices.catalogservice.repositories;

import com.microservices.catalogservice.models.entities.product_inventory.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    List<Brand> findAllByIsActiveIsTrue();
    Optional<Brand> findByCodeAndIsActiveIsTrue(String code);
    Optional<Brand> findByNameAndIsActiveIsTrue(String name);
}
