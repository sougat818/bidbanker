package com.sougat818.bidbanker.users.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class CreateUserRequestValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidCreateUserRequest() {
        CreateUserRequest user = new CreateUserRequest("username", "password", "email@example.com");
        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(user);
        assertThat(violations.size()).isEqualTo(0);
    }

    @Test
    void testUsernameNotNull() {
        CreateUserRequest createUserRequest = new CreateUserRequest(null, "password", "email@example.com");
        Set<String> violationMessages =
                validator.validate(createUserRequest).stream().map(ConstraintViolation::getMessage)
                        .collect(Collectors.toSet());

        Set<String> expectedMessages = Set.of("Username is mandatory", "must not be null");

        assertThat(violationMessages.size()).isEqualTo(2);
        assertThat(violationMessages).containsAll(expectedMessages);

    }

    @Test
    void testUsernameNotBlank() {
        CreateUserRequest createUserRequest = new CreateUserRequest("", "password", "email@example.com");
        Set<String> violationMessages =
                validator.validate(createUserRequest).stream().map(ConstraintViolation::getMessage)
                        .collect(Collectors.toSet());

        Set<String> expectedMessages = Set.of("Username is mandatory", "Username must be between 3 and 20 characters");

        assertThat(violationMessages.size()).isEqualTo(2);
        assertThat(violationMessages).containsAll(expectedMessages);
    }

    @Test
    void testUsernameNotValid() {
        CreateUserRequest createUserRequest = new CreateUserRequest("a", "password", "email@example.com");
        Set<String> violationMessages =
                validator.validate(createUserRequest).stream().map(ConstraintViolation::getMessage)
                        .collect(Collectors.toSet());

        Set<String> expectedMessages = Set.of("Username must be between 3 and 20 characters");

        assertThat(violationMessages.size()).isEqualTo(1);
        assertThat(violationMessages).containsAll(expectedMessages);
    }

    @Test
    void testEmailNotNull() {
        CreateUserRequest createUserRequest = new CreateUserRequest("username", "password", null);
        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(createUserRequest);
        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Email is mandatory");
    }


    @Test
    void testEmailValid() {
        CreateUserRequest createUserRequest = new CreateUserRequest("username", "password", "invalid-email");
        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(createUserRequest);
        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Email should be valid");
    }

    @Test
    void testPasswordNotNull() {
        CreateUserRequest createUserRequest = new CreateUserRequest("username", null, "email@example.com");
        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(createUserRequest);
        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Password is mandatory");
    }

    @Test
    void testPasswordNotValid() {
        CreateUserRequest createUserRequest = new CreateUserRequest("username", "a", "email@example.com");
        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(createUserRequest);
        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Password must be at least 6 characters");
    }
}