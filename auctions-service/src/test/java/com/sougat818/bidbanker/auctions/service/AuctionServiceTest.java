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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuctionsServiceTest {

    private static final OffsetDateTime now = OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC);

    @Mock
    private AuctionRepository auctionRepository;

    @Mock
    private BidRepository bidRepository;

    @InjectMocks
    private AuctionService auctionService;

    private Auction auction;
    private CreateAuctionRequest createAuctionRequest;
    private Bid bid;
    private CreateBidRequest createBidRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        auction = new Auction(1L, "Product Name", 100.0, 1L, AuctionStatus.OPEN, now);
        createAuctionRequest = new CreateAuctionRequest("Product Name", 100.0, 1L);
        bid = new Bid(1L, 1L, 150.0, 2L, now);
        createBidRequest = new CreateBidRequest(1L, 150.0, 2L);
    }

    @Test
    void testCreateAuction_Success() {
        when(auctionRepository.save(any(Auction.class))).thenReturn(Mono.just(auction));
        Auction auctionRequest = new Auction(1L, "Product Name", 100.0, 1L, AuctionStatus.OPEN, now);

        Auction auctionResponse = new Auction(1L, "Product Name", 100.0, 1L, AuctionStatus.OPEN, now);

        when(auctionRepository.findFirstBySellerIdAndProductNameOrderByCreatedAtDesc(any(Long.class),
                any(String.class)))
                .thenReturn(Mono.just(auctionResponse));

        Mono<Auction> result = auctionService.createAuction(auctionRequest);

        StepVerifier.create(result)
                .expectNext(auctionResponse)
                .verifyComplete();
    }

    @Test
    void testPlaceBid_Success() {
        when(auctionRepository.findByIdAndStatus(1L, AuctionStatus.OPEN)).thenReturn(Mono.just(auction));
        when(bidRepository.save(any(Bid.class))).thenReturn(Mono.just(bid));

        Mono<Bid> result = auctionService.placeBid(bid);

        StepVerifier.create(result)
                .expectNext(bid)
                .verifyComplete();
    }

    @Test
    void testPlaceBid_AuctionNotFound() {
        when(auctionRepository.findByIdAndStatus(1L, AuctionStatus.OPEN)).thenReturn(Mono.empty());

        Mono<Bid> result = auctionService.placeBid(bid);

        StepVerifier.create(result)
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    void testPlaceBid_BidTooLow() {
        auction.setMinimumBid(200.0);
        when(auctionRepository.findByIdAndStatus(1L, AuctionStatus.OPEN)).thenReturn(Mono.just(auction));

        Mono<Bid> result = auctionService.placeBid(bid);

        StepVerifier.create(result)
                .expectError(BadRequestException.class)
                .verify();
    }

    @Test
    void testPlaceBid_SellerBidding() {
        bid.setBuyerId(1L);
        when(auctionRepository.findByIdAndStatus(1L, AuctionStatus.OPEN)).thenReturn(Mono.just(auction));

        Mono<Bid> result = auctionService.placeBid(bid);

        StepVerifier.create(result)
                .expectError(BadRequestException.class)
                .verify();
    }

    @Test
    void testCloseAuction_Success() {
        when(auctionRepository.findByIdAndStatus(1L, AuctionStatus.OPEN)).thenReturn(Mono.just(auction));
        when(bidRepository.findTopByAuctionIdOrderByAmountDescTimestampAsc(1L)).thenReturn(Mono.just(bid));
        when(auctionRepository.save(any(Auction.class))).thenReturn(Mono.just(auction));

        Mono<Bid> result = auctionService.closeAuction(1L);

        StepVerifier.create(result)
                .expectNext(bid)
                .verifyComplete();
    }

    @Test
    void testCloseAuction_NoBids() {
        when(auctionRepository.findByIdAndStatus(1L, AuctionStatus.OPEN)).thenReturn(Mono.just(auction));
        when(bidRepository.findTopByAuctionIdOrderByAmountDescTimestampAsc(1L)).thenReturn(Mono.empty());

        Mono<Bid> result = auctionService.closeAuction(1L);

        StepVerifier.create(result)
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    void testToEntity_Auction() {
        Auction result = auctionService.toEntity(createAuctionRequest);
        assertThat(result.getProductName()).isEqualTo(createAuctionRequest.productName());
        assertThat(result.getMinimumBid()).isEqualTo(createAuctionRequest.minimumBid());
        assertThat(result.getSellerId()).isEqualTo(createAuctionRequest.sellerId());
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(AuctionStatus.OPEN);
    }

    @Test
    void testToResponse_Auction() {
        CreateAuctionResponse result = auctionService.toResponse(auction);
        assertThat(result.id()).isEqualTo(auction.getId());
        assertThat(result.productName()).isEqualTo(auction.getProductName());
        assertThat(result.minimumBid()).isEqualTo(auction.getMinimumBid());
        assertThat(result.sellerId()).isEqualTo(auction.getSellerId());
        assertThat(result.status()).isEqualTo(auction.getStatus().name());
        assertThat(result.createdAt()).isEqualTo(auction.getCreatedAt());
    }

    @Test
    void testToEntity_Bid() {
        Bid result = auctionService.toEntity(createBidRequest);
        assertThat(result.getAuctionId()).isEqualTo(createBidRequest.auctionId());
        assertThat(result.getAmount()).isEqualTo(createBidRequest.amount());
        assertThat(result.getBuyerId()).isEqualTo(createBidRequest.buyerId());
        assertThat(result.getTimestamp()).isNotNull();
    }

    @Test
    void testToResponse_Bid() {
        CreateBidResponse result = auctionService.toResponse(bid);
        assertThat(result.id()).isEqualTo(bid.getId());
        assertThat(result.auctionId()).isEqualTo(bid.getAuctionId());
        assertThat(result.amount()).isEqualTo(bid.getAmount());
        assertThat(result.buyerId()).isEqualTo(bid.getBuyerId());
        assertThat(result.timestamp()).isEqualTo(bid.getTimestamp());
    }
}