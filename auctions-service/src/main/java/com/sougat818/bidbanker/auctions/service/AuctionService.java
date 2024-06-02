package com.sougat818.bidbanker.auctions.service;

import com.sougat818.bidbanker.auctions.domain.Auction;
import com.sougat818.bidbanker.auctions.domain.AuctionStatus;
import com.sougat818.bidbanker.auctions.domain.Bid;
import com.sougat818.bidbanker.auctions.dto.CreateAuctionRequest;
import com.sougat818.bidbanker.auctions.dto.CreateAuctionResponse;
import com.sougat818.bidbanker.auctions.dto.CreateBidRequest;
import com.sougat818.bidbanker.auctions.dto.CreateBidResponse;
import com.sougat818.bidbanker.auctions.exceptions.BadRequestException;
import com.sougat818.bidbanker.auctions.exceptions.NotFoundException;
import com.sougat818.bidbanker.auctions.repository.AuctionRepository;
import com.sougat818.bidbanker.auctions.repository.BidRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;

    @Transactional
    public Mono<Auction> createAuction(Auction auction) {
        return auctionRepository.save(auction)
                // https://github.com/spring-projects/spring-data-relational/issues/1274
                .flatMap(a -> auctionRepository.findFirstBySellerIdAndProductNameOrderByCreatedAtDesc(a.getSellerId(),
                        a.getProductName()));
    }

    @Transactional
    public Mono<Bid> placeBid(Bid bid) {
        return auctionRepository.findByIdAndStatus(bid.getAuctionId(), AuctionStatus.OPEN)
                .switchIfEmpty(Mono.error(new NotFoundException("Auction not found or closed")))
                .flatMap(auction -> {
                    if (bid.getAmount() <= auction.getMinimumBid()) {
                        return Mono.error(new BadRequestException("Bid amount must be higher than the minimum " +
                                                                  "bid"));
                    }
                    if (bid.getBuyerId().equals(auction.getSellerId())) {
                        return Mono.error(new BadRequestException("Sellers cannot bid on their own auction"));
                    }
                    return bidRepository.save(bid);
                });
    }

    @Transactional
    public Mono<Bid> closeAuction(Long auctionId) {
        return auctionRepository.findByIdAndStatus(auctionId, AuctionStatus.OPEN)
                .switchIfEmpty(Mono.error(new NotFoundException("Auction not found or already closed")))
                .flatMap(auction -> bidRepository.findTopByAuctionIdOrderByAmountDescTimestampAsc(auction.getId())
                        .switchIfEmpty(Mono.error(new NotFoundException("Cannot close auction without bids")))
                        .flatMap(highestBid -> {
                            auction.setStatus(AuctionStatus.CLOSED);
                            return auctionRepository.save(auction).thenReturn(highestBid);
                        }));
    }

    public Auction toEntity(CreateAuctionRequest auctionRequest) {
        Auction auction = new Auction();
        auction.setProductName(auctionRequest.productName());
        auction.setMinimumBid(auctionRequest.minimumBid());
        auction.setSellerId(auctionRequest.sellerId());
        auction.setStatus(AuctionStatus.OPEN);
        auction.setCreatedAt(OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC));
        return auction;
    }

    public CreateAuctionResponse toResponse(Auction auction) {
        return new CreateAuctionResponse(auction.getId(), auction.getProductName(),
                auction.getMinimumBid(), auction.getSellerId(), auction.getStatus().name(), auction.getCreatedAt());
    }

    public Bid toEntity(CreateBidRequest bidRequest) {
        Bid bid = new Bid();
        bid.setAuctionId(bidRequest.auctionId());
        bid.setAmount(bidRequest.amount());
        bid.setBuyerId(bidRequest.buyerId());
        bid.setTimestamp(OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC));
        return bid;
    }

    public CreateBidResponse toResponse(Bid bid) {
        return new CreateBidResponse(bid.getId(), bid.getAuctionId(), bid.getAmount(), bid.getBuyerId(),
                bid.getTimestamp());
    }
}