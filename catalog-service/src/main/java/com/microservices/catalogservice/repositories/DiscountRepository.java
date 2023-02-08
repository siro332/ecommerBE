package com.microservices.catalogservice.repositories;

import com.microservices.catalogservice.models.entities.product_inventory.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {
    List<Discount> findAll();
    List<Discount> findByCode(String code);
    List<Discount> findByName(String name);
}
