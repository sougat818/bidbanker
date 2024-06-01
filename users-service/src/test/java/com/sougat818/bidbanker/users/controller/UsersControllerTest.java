package com.sougat818.bidbanker.users.controller;

import com.sougat818.bidbanker.users.domain.BidUser;
import com.sougat818.bidbanker.users.dto.CreateUserRequest;
import com.sougat818.bidbanker.users.dto.CreateUserResponse;
import com.sougat818.bidbanker.users.service.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UsersControllerTest {

    @Mock
    private UsersService usersService;

    @InjectMocks
    private UsersController usersController;

    private WebTestClient webTestClient;

    private BidUser bidUser;
    private CreateUserRequest createUserRequest;
    private CreateUserResponse createUserResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        webTestClient = WebTestClient.bindToController(usersController).build();
        bidUser = new BidUser(1L, "username", "email@example.com", "password");
        createUserRequest = new CreateUserRequest("username", "password", "email@example.com");
        createUserResponse = new CreateUserResponse(1L, "username", "password", "email@example.com");
    }

    @Test
    void testCreateUser_Success() {
        when(usersService.registerUser(any(BidUser.class))).thenReturn(Mono.just(bidUser));
        when(usersService.toResponse(any(BidUser.class))).thenReturn(createUserResponse);
        when(usersService.toEntity(any(CreateUserRequest.class))).thenReturn(bidUser);

        webTestClient.post()
                .uri("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createUserRequest)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(CreateUserResponse.class)
                .isEqualTo(createUserResponse);
    }

}