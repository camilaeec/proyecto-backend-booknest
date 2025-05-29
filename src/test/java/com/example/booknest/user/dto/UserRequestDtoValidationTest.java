package com.example.booknest.user.dto;

import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserRequestDtoValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        validator = localValidatorFactoryBean;
    }

    @Test
    void shouldFailValidationWhenEmailIsInvalid() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setEmail("invalid-email");
        dto.setNickname("validnick");
        dto.setPassword("password");

        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldFailValidationWhenNicknameIsBlank() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setEmail("valid@example.com");
        dto.setNickname("");
        dto.setPassword("password");

        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldFailValidationWhenPasswordIsTooShort() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setEmail("valid@example.com");
        dto.setNickname("validnick");
        dto.setPassword("123");

        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    void validUserRequestDto_ShouldPassValidation() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setEmail("valid@example.com");
        dto.setNickname("validnick");
        dto.setPassword("password123");
        dto.setName("John");
        dto.setLastname("Doe");
        dto.setPhoneNumber("123456789");

        var violations = validator.validate(dto);
        assertEquals(0, violations.size());
    }
}
