package com.example.booknest.transaction.dto;

import com.example.booknest.book.dto.BookResponse;
import com.example.booknest.user.dto.UserResponseForOtherUsersDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class TransactionResponseDTO {
    private Long idTransaction;
    private Date date;
    private Boolean accepted;
    private UserResponseForOtherUsersDTO buyer;
    private UserResponseForOtherUsersDTO seller;
    private BookResponse book;
    private BookResponse offeredBook;
}
