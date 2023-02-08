package com.microservices.catalogservice.services.impl;

import com.microservices.catalogservice.configs.JwtService;
import com.microservices.catalogservice.converters.DtoConverter;
import com.microservices.catalogservice.models.auth.AuthenticationRequest;
import com.microservices.catalogservice.models.auth.AuthenticationResponse;
import com.microservices.catalogservice.models.auth.RegisterRequest;
import com.microservices.catalogservice.models.entities.user.Role;
import com.microservices.catalogservice.models.entities.user.User;
import com.microservices.catalogservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final DtoConverter dtoConverter;
  public AuthenticationResponse register(RegisterRequest request) {
    var user = User.builder()
        .firstname(request.getFirstname())
        .lastname(request.getLastname())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(Role.USER)
        .build();
    repository.save(user);
    var jwtToken = jwtService.generateToken(user);
    return AuthenticationResponse.builder()
            .user(dtoConverter.userEntityToDto(repository.findByEmail(user.getEmail()).get()))
        .token(jwtToken)
        .build();
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );
    var user = repository.findByEmail(request.getEmail())
        .orElseThrow();
    var jwtToken = jwtService.generateToken(user);
    return AuthenticationResponse.builder()
            .user(dtoConverter.userEntityToDto(user))
        .token(jwtToken)
        .build();
  }

  public User getUserFromToken(String token) {
    Optional<User> user = repository.findByEmail(jwtService.extractUsername(token));
    return user.orElse(null);
  }
}
