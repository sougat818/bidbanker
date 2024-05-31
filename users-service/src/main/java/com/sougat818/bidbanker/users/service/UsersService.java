package com.sougat818.bidbanker.users.service;

import com.sougat818.bidbanker.users.domain.User;
import com.sougat818.bidbanker.users.dto.CreateUserRequest;
import com.sougat818.bidbanker.users.dto.CreateUserResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
public class UsersService {
    @Transactional
    public Mono<User> registerUser(User user) {
        return Mono.just(user);
    }


    public User toEntity(CreateUserRequest request) {
        return new User(null, request.username(), request.email(), request.password());
    }

    public CreateUserResponse toResponse(User user) {
        return new CreateUserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getPassword());
    }
}
