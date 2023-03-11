package com.microservices.catalogservice.controllers;

import java.util.List;


import com.microservices.catalogservice.models.entities.Review;
import com.microservices.catalogservice.services.impl.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/review")
public class ReviewController {

    @Autowired
    private final ReviewService service;

    @GetMapping
    @Cacheable(value = "reviews")
    public ResponseEntity<?> getReviews() throws EntityNotFoundException {
        List<Review> productReviews = service.findAll();
        if (productReviews.isEmpty()) {
            throw new EntityNotFoundException("No reviews found.");
        }
        return ResponseEntity.ok(productReviews);
    }

    @PostMapping
    public ResponseEntity<?> insertReview(@RequestBody Review review)
            throws EntityNotFoundException {
        Review reviewInserted = service.save(review);
        return ResponseEntity.ok(reviewInserted);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Long id) throws EntityNotFoundException {
        service.deleteReview(id);
        return ResponseEntity.ok("Deleted Review with id : " + id);
    }

}
