package com.sougat818.bidbanker.auctions.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bid {

    @Id
    private Long id;

    private Long auctionId;

    private Double amount;

    private Long buyerId;

    private OffsetDateTime timestamp;
}