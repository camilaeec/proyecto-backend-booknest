package com.example.booknest.transaction.dto;

import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TransactionRequestDtoValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        validator = localValidatorFactoryBean;
    }

    @Test
    void shouldFailValidationWhenBookIdIsNull() {
        TransactionRequestDTO dto = new TransactionRequestDTO();
        dto.setBookId(null);
        dto.setOfferedBookId(1L);
        dto.setSellerNickname("seller1");

        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldFailValidationWhenOfferedBookIdIsNull() {
        TransactionRequestDTO dto = new TransactionRequestDTO();
        dto.setBookId(1L);
        dto.setOfferedBookId(null);
        dto.setSellerNickname("seller1");

        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldFailValidationWhenSellerNicknameIsBlank() {
        TransactionRequestDTO dto = new TransactionRequestDTO();
        dto.setBookId(1L);
        dto.setOfferedBookId(2L);
        dto.setSellerNickname("");

        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    void validTransactionRequestDto_ShouldPassValidation() {
        TransactionRequestDTO dto = new TransactionRequestDTO();
        dto.setBookId(1L);
        dto.setOfferedBookId(2L);
        dto.setSellerNickname("seller1");

        var violations = validator.validate(dto);
        assertEquals(0, violations.size());
    }
}
