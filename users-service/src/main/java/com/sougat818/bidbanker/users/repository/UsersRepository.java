package com.sougat818.bidbanker.users.repository;

import com.sougat818.bidbanker.users.domain.BidUser;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface UsersRepository extends R2dbcRepository<BidUser, Long> {

    Mono<BidUser> findByUsername(String username);
}
