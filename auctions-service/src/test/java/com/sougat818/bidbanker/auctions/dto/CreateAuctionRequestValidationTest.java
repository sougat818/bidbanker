// CreateAuctionRequestValidationTest.java
package com.sougat818.bidbanker.auctions.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class CreateAuctionRequestValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidCreateAuctionRequest() {
        CreateAuctionRequest auction = new CreateAuctionRequest("Product Name", 100.0, 1L);
        Set<ConstraintViolation<CreateAuctionRequest>> violations = validator.validate(auction);
        assertThat(violations.size()).isEqualTo(0);
    }

    @Test
    void testProductNameNotNull() {
        CreateAuctionRequest createAuctionRequest = new CreateAuctionRequest(null, 100.0, 1L);
        Set<String> violationMessages =
                validator.validate(createAuctionRequest).stream().map(ConstraintViolation::getMessage)
                        .collect(Collectors.toSet());

        Set<String> expectedMessages = Set.of("must not be null");

        assertThat(violationMessages.size()).isEqualTo(1);
        assertThat(violationMessages).containsAll(expectedMessages);
    }

    @Test
    void testProductNameNotEmpty() {
        CreateAuctionRequest createAuctionRequest = new CreateAuctionRequest("", 100.0, 1L);
        Set<String> violationMessages =
                validator.validate(createAuctionRequest).stream().map(ConstraintViolation::getMessage)
                        .collect(Collectors.toSet());

        Set<String> expectedMessages = Set.of("size must be between 1 and 255");

        assertThat(violationMessages.size()).isEqualTo(1);
        assertThat(violationMessages).containsAll(expectedMessages);
    }

    @Test
    void testSellerIdNotNull() {
        CreateAuctionRequest createAuctionRequest = new CreateAuctionRequest("Product Name", 100.0, null);
        Set<String> violationMessages =
                validator.validate(createAuctionRequest).stream().map(ConstraintViolation::getMessage)
                        .collect(Collectors.toSet());

        Set<String> expectedMessages = Set.of("must not be null");

        assertThat(violationMessages.size()).isEqualTo(1);
        assertThat(violationMessages).containsAll(expectedMessages);
    }

    @Test
    void testMinimumBidNotNull() {
        CreateAuctionRequest createAuctionRequest = new CreateAuctionRequest("Product Name", null, 1L);
        Set<String> violationMessages =
                validator.validate(createAuctionRequest).stream().map(ConstraintViolation::getMessage)
                        .collect(Collectors.toSet());

        Set<String> expectedMessages = Set.of("must not be null");

        assertThat(violationMessages.size()).isEqualTo(1);
        assertThat(violationMessages).containsAll(expectedMessages);
    }

    @Test
    void testMinimumBidGreaterThan0() {
        CreateAuctionRequest createAuctionRequest = new CreateAuctionRequest("Product Name", 0.0, 1L);
        Set<String> violationMessages =
                validator.validate(createAuctionRequest).stream().map(ConstraintViolation::getMessage)
                        .collect(Collectors.toSet());

        Set<String> expectedMessages = Set.of("must be greater than or equal to 1");

        assertThat(violationMessages.size()).isEqualTo(1);
        assertThat(violationMessages).containsAll(expectedMessages);
    }
}