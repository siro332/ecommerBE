package com.microservices.catalogservice.repositories;

import com.microservices.catalogservice.models.entities.Category;
import com.microservices.catalogservice.models.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Optional<Product> findByCodeAndIsActiveIsTrue(String code);
    Page<Product> findByIsActiveIsTrue(Pageable pageable);
    Page<Product> findByCategoriesContainsAndIsActiveIsTrue(Category category, Pageable pageable);
    List<Product> findByCategoriesContainsAndIsActiveIsTrue(Category category);
    @Query(nativeQuery = true,value = "SELECT p.* " +
            "                    FROM" +
            "                     products p join products_categories on p.id = products_categories.product_id" +
            "                    join categories c on products_categories.categories_id = c.id" +
            "                    join product_inventories pi on p.id = pi.product_id" +
            "                    join brands b on p.brand_id = b.id" +
            "                    WHERE p.name like :productName " +
            "                    and c.code like :categoryCode" +
            "                    and pi.retail_price >= :lowestPrice " +
            "                    and pi.retail_price <= :highestPrice" +
            "                    and b.name like :brandName" +
            "                    and p.is_active = true" +
            "                    group by p.id")
    List<Product> search(String productName, String categoryCode, long lowestPrice, long highestPrice, String brandName);


}