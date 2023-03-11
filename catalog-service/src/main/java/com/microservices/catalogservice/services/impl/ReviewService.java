package com.microservices.catalogservice.services.impl;

import com.microservices.catalogservice.models.entities.Review;
import com.microservices.catalogservice.repositories.ReviewRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
@Builder
@Service
public class ReviewService {
    private final ReviewRepository repository;
    public List<Review> findAll() {
        return repository.findAll();
    }
    public Review save(Review review) throws EntityNotFoundException {
        return repository.save(review);
    }

    public void deleteReview(Long reviewId) throws EntityNotFoundException {
        Optional<Review> findById = repository.findById(reviewId);
        if (findById.isPresent()) {
            repository.delete(findById.get());
        } else {
            throw new EntityNotFoundException("Review with id = " + reviewId + " not found.");
        }
    }

    public List<Review> findByProductID(Long productId){
        return repository.findByProductId(productId);
    }
}
