package com.microservices.catalogservice.repositories;

import com.microservices.catalogservice.models.entities.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
    List<Media> findAll();
    List<Media> findByCode(String code);
    List<Media> findByType(String type);
}
