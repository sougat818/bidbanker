package com.sougat818.bidbanker.users.controller;

import com.sougat818.bidbanker.users.dto.CreateUserRequest;
import com.sougat818.bidbanker.users.dto.CreateUserResponse;
import com.sougat818.bidbanker.users.service.UsersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RestControllerAdvice
@RequestMapping("/users")
@Validated
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @PostMapping("/register")
    public Mono<ResponseEntity<CreateUserResponse>> registerUser(@RequestBody @Valid CreateUserRequest user) {
        return Mono.just(user)
                .map(usersService::toEntity)
                .flatMap(usersService::registerUser)
                .map(usersService::toResponse)
                .map(ResponseEntity::ok);
    }
}