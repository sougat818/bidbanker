package com.sougat818.bidbanker.auctions.repository;

import com.sougat818.bidbanker.auctions.domain.Bid;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BidRepository extends R2dbcRepository<Bid, Long> {
    Flux<Bid> findByAuctionId(Long auctionId);
    Mono<Bid> findTopByAuctionIdOrderByAmountDescTimestampAsc(Long auctionId);
}