package com.microservices.catalogservice.repositories;

import com.microservices.catalogservice.models.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAllByIsDeletedIsFalse(Pageable pageable);
    List<Order> findByUserCodeAndIsDeletedIsFalse(String code);
    Order findByCodeAndIsDeletedIsFalse(String code);
    Optional<Order> findByIdAndIsDeletedIsFalse(Long id);
    List<Order> findByCreatedAtBetweenAndStatus(Date from, Date to,String status);
    List<Order> findAllByIsDeletedIsFalse();
}
