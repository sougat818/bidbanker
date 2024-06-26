package com.sougat818.bidbanker.users.service;

import com.sougat818.bidbanker.users.domain.BidUser;
import com.sougat818.bidbanker.users.dto.CreateUserRequest;
import com.sougat818.bidbanker.users.dto.CreateUserResponse;
import com.sougat818.bidbanker.users.exceptions.ConflictException;
import com.sougat818.bidbanker.users.repository.UsersRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsersService {

    private final UsersRepository usersRepository;

    @Transactional
    public Mono<BidUser> registerUser(@NonNull BidUser bidUser) {
        return usersRepository.save(bidUser)
                .map(BidUser::getUsername)
                // https://github.com/spring-projects/spring-data-relational/issues/1274
                .flatMap(usersRepository::findByUsername)
                .onErrorResume(DuplicateKeyException.class,
                        e -> Mono.error(new ConflictException("User already exists.")));

    }


    public BidUser toEntity(CreateUserRequest request) {
        return new BidUser(null, request.username(), request.password(), request.email());
    }

    public CreateUserResponse toResponse(BidUser bidUser) {
        return new CreateUserResponse(bidUser.getId(), bidUser.getUsername(),
                bidUser.getPassword(), bidUser.getEmail());
    }
}
