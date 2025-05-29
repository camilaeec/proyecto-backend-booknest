package com.example.booknest.transaction.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class TransactionRequestDTO {
    private Date date;
    private Boolean accepted;
    private String sellerNickname;  // Nickname del vendedor
    private Long bookId;           // ID del libro que se quiere comprar
    private Long offeredBookId;   // ID del libro que se ofrece en intercambio
}
