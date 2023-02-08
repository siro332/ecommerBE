package com.microservices.catalogservice.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.microservices.catalogservice.models.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByEmail(String email);
  List<User> findByCreatedDateBetween(Date begin, Date end);
}
