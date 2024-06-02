package com.sougat818.bidbanker.auctions.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;
import java.util.Map;

@Getter
public class BidBankerErrorResponse {
    private final OffsetDateTime timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final Map<String, String> errors;

    public BidBankerErrorResponse(HttpStatus status, String message) {
        this.timestamp = OffsetDateTime.now();
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
        this.errors = null;

    }

    public BidBankerErrorResponse(HttpStatus status, String message, Map<String, String> errors) {
        this.timestamp = OffsetDateTime.now();
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
        this.errors = errors;
    }

}