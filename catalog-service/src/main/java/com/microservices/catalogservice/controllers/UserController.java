package com.microservices.catalogservice.controllers;

import com.microservices.catalogservice.models.dtos.CartDto;
import com.microservices.catalogservice.models.entities.Cart;
import com.microservices.catalogservice.models.entities.CartItem;
import com.microservices.catalogservice.models.entities.user.User;
import com.microservices.catalogservice.repositories.UserRepository;
import com.microservices.catalogservice.services.impl.AuthenticationService;
import com.microservices.catalogservice.services.impl.CartServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final RedisTemplate<String, Cart> redisTemplate;

    @GetMapping("")
    public ResponseEntity<?> getAllUser(){
        return ResponseEntity.ok(userRepository.findAll());
    }



    @GetMapping("/stats")
    public ResponseEntity<?> getUserStat() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        HashMap<Integer,Long> result = new HashMap<>();
        while (calendar.get(Calendar.YEAR) == year){
            calendar.set(Calendar.DAY_OF_MONTH,1);
            Date from = calendar.getTime();
            calendar.set(Calendar.DAY_OF_MONTH,-1);
            Date to = calendar.getTime();
            Long count = (long) userRepository.findByCreatedDateBetween(from, to).size();
            result.put(calendar.get(Calendar.MONTH),count);
            calendar.add(Calendar.MONTH,-1);
        }
        return ResponseEntity.ok(result);
    }
}
