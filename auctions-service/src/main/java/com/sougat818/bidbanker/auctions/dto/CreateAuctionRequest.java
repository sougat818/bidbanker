package com.sougat818.bidbanker.auctions.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateAuctionRequest(
        @NotNull @Size(min = 1, max = 255) String productName,
        @NotNull @Min(1) Double minimumBid,
        @NotNull Long sellerId
) {
}