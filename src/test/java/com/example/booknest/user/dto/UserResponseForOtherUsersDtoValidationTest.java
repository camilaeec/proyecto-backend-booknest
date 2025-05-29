package com.example.booknest.user.dto;

import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserResponseForOtherUsersDtoValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        validator = localValidatorFactoryBean;
    }

    @Test
    void shouldFailValidationWhenIdIsNull() {
        UserResponseForOtherUsersDTO dto = new UserResponseForOtherUsersDTO();
        dto.setId(null);
        dto.setNickname("testuser");

        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldFailValidationWhenNicknameIsBlank() {
        UserResponseForOtherUsersDTO dto = new UserResponseForOtherUsersDTO();
        dto.setId(1L);
        dto.setNickname("");

        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    void validUserResponseForOtherUsersDto_ShouldPassValidation() {
        UserResponseForOtherUsersDTO dto = new UserResponseForOtherUsersDTO();
        dto.setId(1L);
        dto.setNickname("testuser");

        var violations = validator.validate(dto);
        assertEquals(0, violations.size());
    }
}