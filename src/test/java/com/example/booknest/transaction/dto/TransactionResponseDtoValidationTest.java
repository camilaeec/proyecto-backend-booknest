package com.example.booknest.transaction.dto;

import com.example.booknest.book.dto.BookResponse;
import com.example.booknest.user.dto.UserResponseForOtherUsersDTO;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TransactionResponseDtoValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        validator = localValidatorFactoryBean;
    }

    @Test
    void shouldFailValidationWhenIdTransactionIsNull() {
        TransactionResponseDTO dto = createValidTransactionResponseDTO();
        dto.setIdTransaction(null);

        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldFailValidationWhenDateIsNull() {
        TransactionResponseDTO dto = createValidTransactionResponseDTO();
        dto.setDate(null);

        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldFailValidationWhenBookIsNull() {
        TransactionResponseDTO dto = createValidTransactionResponseDTO();
        dto.setBook(null);

        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    private TransactionResponseDTO createValidTransactionResponseDTO() {
        UserResponseForOtherUsersDTO buyer = new UserResponseForOtherUsersDTO();
        buyer.setId(1L);
        buyer.setNickname("buyer1");

        UserResponseForOtherUsersDTO seller = new UserResponseForOtherUsersDTO();
        seller.setId(2L);
        seller.setNickname("seller1");

        BookResponse book = new BookResponse();
        book.setIdBook(1L);

        BookResponse offeredBook = new BookResponse();
        offeredBook.setIdBook(2L);

        TransactionResponseDTO dto = new TransactionResponseDTO();
        dto.setIdTransaction(1L);
        dto.setDate(new Date());
        dto.setAccepted(false);
        dto.setBuyer(buyer);
        dto.setSeller(seller);
        dto.setBook(book);
        dto.setOfferedBook(offeredBook);

        return dto;
    }
}
