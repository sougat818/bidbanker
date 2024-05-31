package com.sougat818.bidbanker.users.controller;

import com.sougat818.bidbanker.users.dto.CreateUserRequest;
import com.sougat818.bidbanker.users.dto.CreateUserResponse;
import com.sougat818.bidbanker.users.service.UsersService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RestControllerAdvice
@RequestMapping("/users")
@Validated
public class UsersController {

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/register")
    public Mono<CreateUserResponse> registerUser(@RequestBody Mono<@Valid CreateUserRequest> user) {
        return user.map(usersService::toEntity)
                .flatMap(usersService::registerUser)
                .map(usersService::toResponse);
    }
}