package com.sougat818.bidbanker.users.exceptions;

import com.sougat818.bidbanker.users.dto.BidBankerErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleConflictException() {
        ConflictException exception = new ConflictException("Conflict occurred");
        ResponseEntity<BidBankerErrorResponse> response = globalExceptionHandler.handleConflictException(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody().getError()).isEqualTo("Conflict");
        assertThat(response.getBody().getMessage()).isEqualTo("Conflict occurred");
        assertThat(response.getBody().getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(response.getBody().getErrors()).isNull();
        assertThat(response.getBody().getTimestamp()).isNotNull();
    }

    @Test
    void handleNotFoundException() {
        NotFoundException exception = new NotFoundException("Not found");
        ResponseEntity<BidBankerErrorResponse> response = globalExceptionHandler.handleNotFoundException(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getError()).isEqualTo("Not Found");
        assertThat(response.getBody().getMessage()).isEqualTo("Not found");
        assertThat(response.getBody().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getBody().getErrors()).isNull();
        assertThat(response.getBody().getTimestamp()).isNotNull();


    }

    @Test
    void handleBadRequestException() {
        BadRequestException exception = new BadRequestException("Bad request");
        ResponseEntity<BidBankerErrorResponse> response = globalExceptionHandler.handleBadRequestException(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getError()).isEqualTo("Bad Request");
        assertThat(response.getBody().getMessage()).isEqualTo("Bad request");
        assertThat(response.getBody().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getBody().getErrors()).isNull();
        assertThat(response.getBody().getTimestamp()).isNotNull();


    }

    @Test
    void handleRuntimeException() {
        RuntimeException exception = new RuntimeException("Runtime error");
        ResponseEntity<BidBankerErrorResponse> response = globalExceptionHandler.handleRuntimeException(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().getError()).isEqualTo("Internal Server Error");
        assertThat(response.getBody().getMessage()).isNull();
        assertThat(response.getBody().getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(response.getBody().getErrors()).isNull();
        assertThat(response.getBody().getTimestamp()).isNotNull();


    }

    @Test
    void handleValidationExceptions() {
        BindException bindException = new BindException(new Object(), "target");
        bindException.addError(new FieldError("objectName", "fieldName", "defaultMessage"));
        WebExchangeBindException exception = new WebExchangeBindException(null, bindException.getBindingResult());
        ResponseEntity<BidBankerErrorResponse> response = globalExceptionHandler.handleValidationExceptions(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getError()).startsWith("Bad Request");
        assertThat(response.getBody().getMessage()).startsWith("Validation Failed");
        assertThat(response.getBody().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getBody().getErrors()).isEqualTo(Map.of("fieldName", "defaultMessage"));
        assertThat(response.getBody().getTimestamp()).isNotNull();

    }
}