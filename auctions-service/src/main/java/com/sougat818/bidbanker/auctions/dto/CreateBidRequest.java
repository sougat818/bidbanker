package com.sougat818.bidbanker.auctions.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

public record CreateBidRequest(
        @NotNull Long auctionId,
        @NotNull @Min(1) Double amount,
        @NotNull Long buyerId
) {}