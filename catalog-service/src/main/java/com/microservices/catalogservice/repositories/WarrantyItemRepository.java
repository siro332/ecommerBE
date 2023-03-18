package com.microservices.catalogservice.repositories;

import com.microservices.catalogservice.models.entities.WarrantyItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WarrantyItemRepository extends JpaRepository<WarrantyItem, Integer> {

    List<WarrantyItem> findByProductSerialContains(String productSerial);
    WarrantyItem findById(Long id);
}
