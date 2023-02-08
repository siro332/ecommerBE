package com.microservices.catalogservice.repositories;

import com.microservices.catalogservice.models.entities.Order;
import com.microservices.catalogservice.models.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAllByIsDeletedIsFalse(Pageable pageable);
    List<Order> findByUserCodeAndIsDeletedIsFalse(String code);
    Order findByCodeAndIsDeletedIsFalse(String code);

    List<Order> findByCreatedAtBetween(Date from, Date to);
}
