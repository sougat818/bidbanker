package com.sougat818.bidbanker.users.service;

import com.sougat818.bidbanker.users.domain.BidUser;
import com.sougat818.bidbanker.users.dto.CreateUserRequest;
import com.sougat818.bidbanker.users.dto.CreateUserResponse;
import com.sougat818.bidbanker.users.exceptions.ConflictException;
import com.sougat818.bidbanker.users.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DuplicateKeyException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class UsersServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private UsersService usersService;

    private BidUser bidUser;
    private CreateUserRequest createUserRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bidUser = new BidUser(1L, "username", "password", "email@example.com");
        createUserRequest = new CreateUserRequest("username", "password", "email@example.com");
    }

    @Test
    void testRegisterUser_Success() {
        when(usersRepository.save(any(BidUser.class))).thenReturn(Mono.just(bidUser));
        when(usersRepository.findByUsername(anyString())).thenReturn(Mono.just(bidUser));

        Mono<BidUser> result = usersService.registerUser(bidUser);

        StepVerifier.create(result)
                .expectNext(bidUser)
                .verifyComplete();
    }

    @Test
    void testRegisterUser_UserAlreadyExists() {
        when(usersRepository.save(any(BidUser.class))).thenReturn(Mono.error(new DuplicateKeyException("User already " +
                                                                                                       "exists")));

        Mono<BidUser> result = usersService.registerUser(bidUser);

        StepVerifier.create(result)
                .expectError(ConflictException.class)
                .verify();
    }

    @Test
    void testToEntity() {
        BidUser result = usersService.toEntity(createUserRequest);
        assertThat(result.getUsername()).isEqualTo(createUserRequest.username());
        assertThat(result.getEmail()).isEqualTo(createUserRequest.email());
        assertThat(result.getPassword()).isEqualTo(createUserRequest.password());
    }

    @Test
    void testToResponse() {
        CreateUserResponse result = usersService.toResponse(bidUser);
        assertThat(result.id()).isEqualTo(bidUser.getId());
        assertThat(result.username()).isEqualTo(bidUser.getUsername());
        assertThat(result.email()).isEqualTo(bidUser.getEmail());
        assertThat(result.password()).isEqualTo(bidUser.getPassword());
    }
}