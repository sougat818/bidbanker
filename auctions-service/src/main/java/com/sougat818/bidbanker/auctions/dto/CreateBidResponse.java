package com.sougat818.bidbanker.auctions.dto;

import lombok.Data;

import java.time.OffsetDateTime;

public record CreateBidResponse(
        Long id,
        Long auctionId,
        Double amount,
        Long buyerId,
        OffsetDateTime timestamp
) {}