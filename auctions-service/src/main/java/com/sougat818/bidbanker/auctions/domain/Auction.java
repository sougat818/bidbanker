package com.sougat818.bidbanker.auctions.domain;

import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Auction {

    @Id
    private Long id;

    private String productName;

    private Double minimumBid;

    private Long sellerId;

    @Setter
    private AuctionStatus status;

    private OffsetDateTime createdAt;
}