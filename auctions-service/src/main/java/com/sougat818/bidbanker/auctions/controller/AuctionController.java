package com.sougat818.bidbanker.auctions.controller;

import com.sougat818.bidbanker.auctions.domain.Auction;
import com.sougat818.bidbanker.auctions.domain.Bid;
import com.sougat818.bidbanker.auctions.dto.CreateAuctionRequest;
import com.sougat818.bidbanker.auctions.dto.CreateAuctionResponse;
import com.sougat818.bidbanker.auctions.dto.CreateBidRequest;
import com.sougat818.bidbanker.auctions.dto.CreateBidResponse;
import com.sougat818.bidbanker.auctions.service.AuctionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auctions")
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;

    @PostMapping
    public Mono<ResponseEntity<CreateAuctionResponse>> createAuction(@Valid @RequestBody CreateAuctionRequest auctionRequest) {
        Auction auction = auctionService.toEntity(auctionRequest);
        return auctionService.createAuction(auction)
                .map(auctionService::toResponse)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/bid")
    public Mono<ResponseEntity<CreateBidResponse>> placeBid(@Valid @RequestBody CreateBidRequest bidRequest) {
        Bid bid = auctionService.toEntity(bidRequest);
        return auctionService.placeBid(bid)
                .map(auctionService::toResponse)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/{auctionId}/close")
    public Mono<ResponseEntity<CreateBidResponse>> closeAuction(@PathVariable Long auctionId) {
        return auctionService.closeAuction(auctionId)
                .map(auctionService::toResponse)
                .map(ResponseEntity::ok);
    }
}