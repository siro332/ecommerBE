package com.microservices.catalogservice.controllers;

import com.microservices.catalogservice.configs.JwtService;
import com.microservices.catalogservice.models.auth.AuthenticationRequest;
import com.microservices.catalogservice.models.auth.AuthenticationResponse;
import com.microservices.catalogservice.models.auth.RegisterRequest;
import com.microservices.catalogservice.services.impl.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;
  private final JwtService jwtService;

  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(
      @RequestBody RegisterRequest request
  ) {
    return ResponseEntity.ok(service.register(request));
  }
  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest request
  ) {
    return ResponseEntity.ok(service.authenticate(request));
  }
  @GetMapping("/check-token-expired")
  public  boolean isTokenExpired(@RequestHeader (name="Authorization") String token){
    try {
      return jwtService.isTokenExpired(token);
    }catch (Exception e){
      return true;
    }
  }

}
