package com.microservices.catalogservice.repositories;

import com.microservices.catalogservice.models.entities.product_inventory.ProductInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductInventoryRepository extends JpaRepository<ProductInventory,Long> {
    List<ProductInventory> findByProductCode(String productCode);
    Optional<ProductInventory> findBySku(String code);
    @Query(nativeQuery = true,value = "SELECT product_code " +
            "                    FROM" +
            "                     product_inventories " +
            "                    WHERE product_code IN :productCode " +
            "                    GROUP BY" +
            "                        product_code" +
            "                    ORDER BY" +
            "                      sum(product_inventories.unit_sold) DESC ")
    List<String> findTopSaleProduct(List<String> productCode);
}
