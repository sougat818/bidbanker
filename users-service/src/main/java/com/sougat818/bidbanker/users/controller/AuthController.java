package com.sougat818.bidbanker.users.controller;

import com.sougat818.bidbanker.users.dto.AuthRequest;
import com.sougat818.bidbanker.users.dto.AuthResponse;
import com.sougat818.bidbanker.users.service.UsersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RestControllerAdvice
@RequestMapping("/auth")
@Validated
@RequiredArgsConstructor
public class AuthController {

    private final UsersService usersService;

    @PostMapping("/login")
    public Mono<ResponseEntity<AuthResponse>> login(@RequestBody @Valid AuthRequest authRequest) {
        return usersService.authenticate(authRequest)
                .flatMap(user -> usersService.generateToken(user.getUsername()))
                .map(token -> ResponseEntity.ok(new AuthResponse(token)));
    }
}