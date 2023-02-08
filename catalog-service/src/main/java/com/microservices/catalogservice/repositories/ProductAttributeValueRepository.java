package com.microservices.catalogservice.repositories;

import com.microservices.catalogservice.models.entities.product_inventory.ProductAttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductAttributeValueRepository extends JpaRepository<ProductAttributeValue, Long> {
    List<ProductAttributeValue> findAll();
    List<ProductAttributeValue> findByCode(String code);
    List<ProductAttributeValue> findByProductAttributeId(Integer id);
}

