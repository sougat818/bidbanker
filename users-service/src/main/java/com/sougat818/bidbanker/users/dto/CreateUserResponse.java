package com.sougat818.bidbanker.users.dto;

public record CreateUserResponse(
        Long id,
        String username,
        String password,
        String email
) {
}
