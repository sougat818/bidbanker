package com.sougat818.bidbanker.auctions.repository;

import com.sougat818.bidbanker.auctions.domain.Auction;
import com.sougat818.bidbanker.auctions.domain.AuctionStatus;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface AuctionRepository extends R2dbcRepository<Auction, Long> {
    Mono<Auction> findByIdAndStatus(Long id, AuctionStatus status);

    Mono<Auction> findFirstBySellerIdAndProductNameOrderByCreatedAtDesc(Long sellerId, String productName);
}