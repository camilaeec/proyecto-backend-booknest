package com.example.booknest.user.dto;

import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserResponseDtoValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        validator = localValidatorFactoryBean;
    }

    @Test
    void shouldFailValidationWhenIdUserIsNull() {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setIdUser(null);
        dto.setNickname("testuser");
        dto.setEmail("test@example.com");

        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldFailValidationWhenEmailIsInvalid() {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setIdUser(1L);
        dto.setNickname("testuser");
        dto.setEmail("invalid-email");

        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    void validUserResponseDto_ShouldPassValidation() {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setIdUser(1L);
        dto.setNickname("testuser");
        dto.setEmail("test@example.com");
        dto.setName("Test");
        dto.setLastname("User");
        dto.setPhoneNumber("123456789");

        var violations = validator.validate(dto);
        assertEquals(0, violations.size());
    }
}
