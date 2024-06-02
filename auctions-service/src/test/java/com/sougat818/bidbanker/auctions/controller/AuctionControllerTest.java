package com.sougat818.bidbanker.auctions.controller;

import com.sougat818.bidbanker.auctions.domain.Auction;
import com.sougat818.bidbanker.auctions.domain.AuctionStatus;
import com.sougat818.bidbanker.auctions.domain.Bid;
import com.sougat818.bidbanker.auctions.dto.CreateAuctionRequest;
import com.sougat818.bidbanker.auctions.dto.CreateAuctionResponse;
import com.sougat818.bidbanker.auctions.dto.CreateBidRequest;
import com.sougat818.bidbanker.auctions.dto.CreateBidResponse;
import com.sougat818.bidbanker.auctions.service.AuctionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuctionControllerTest {

    private static final OffsetDateTime now = OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC);

    @InjectMocks
    private AuctionController auctionController;

    @Mock
    private AuctionService auctionService;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
        webTestClient = WebTestClient.bindToController(auctionController).build();

    }

    @Test
    void testCreateAuction() {
        CreateAuctionRequest request = new CreateAuctionRequest("Product Name", 100.0, 1L);
        CreateAuctionResponse response = new CreateAuctionResponse(1L, "Product Name", 100.0, 1L, "OPEN", now);
        Auction auction = new Auction(1L, "Product Name", 100.0, 1L, AuctionStatus.OPEN, now);
        when(auctionService.createAuction(any(Auction.class))).thenReturn(Mono.just(auction));
        when(auctionService.toResponse(any(Auction.class))).thenReturn(response);
        when(auctionService.toEntity(any(CreateAuctionRequest.class))).thenReturn(auction);

        webTestClient.post().uri("/auctions")
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isOk()
                .expectBody(CreateAuctionResponse.class)
                .isEqualTo(response);
    }


    @Test
    void testPlaceBid() {
        CreateBidRequest request = new CreateBidRequest(1L, 2.0, 2L);
        CreateBidResponse response = new CreateBidResponse(1L, 1L, 2.0, 2L, now);
        Bid bid = new Bid(1L, 1L, 2.0, 2L, now);
        when(auctionService.placeBid(any(Bid.class))).thenReturn(Mono.just(bid));
        when(auctionService.toResponse(any(Bid.class))).thenReturn(response);
        when(auctionService.toEntity(any(CreateBidRequest.class))).thenReturn(bid);

        webTestClient.post().uri("/auctions/bid")
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isOk()
                .expectBody(CreateBidResponse.class)
                .isEqualTo(response);
    }

    @Test
    void testCloseAuction() {
        OffsetDateTime now = OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC);
        CreateBidResponse response = new CreateBidResponse(1L, 1L, 2.0, 2L, now);
        Bid bid = new Bid(1L, 1L, 2.0, 2L, now);
        when(auctionService.closeAuction(1L)).thenReturn(Mono.just(bid));
        when(auctionService.toResponse(any(Bid.class))).thenReturn(response);

        webTestClient.post().uri("/auctions/1/close")
                .exchange()
                .expectStatus().isOk()
                .expectBody(CreateBidResponse.class)
                .isEqualTo(response);
    }
}