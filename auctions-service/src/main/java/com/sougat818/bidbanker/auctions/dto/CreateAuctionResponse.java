package com.sougat818.bidbanker.auctions.dto;

import lombok.Data;

import java.time.OffsetDateTime;

public record CreateAuctionResponse(
        Long id,
        String productName,
        Double minimumBid,
        Long sellerId,
        String status,
        OffsetDateTime createdAt
) {}