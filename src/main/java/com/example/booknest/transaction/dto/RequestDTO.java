package com.example.booknest.transaction.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequestDTO {
    private Long idTransaction;
    private Long wantedId;
    private Long offeredId;
    private Long buyerId;
    private Long sellerId;
    private Boolean accepted;
}
