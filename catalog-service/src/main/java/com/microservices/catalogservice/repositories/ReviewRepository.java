package com.microservices.catalogservice.repositories;

import java.util.List;
import java.util.Optional;

import com.microservices.catalogservice.models.entities.Review;
import com.microservices.catalogservice.models.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    public List<Review> findByProductId(Long productId);
    public Optional<Review> findById(Long id);
}
