package com.example.booknest.book.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PriceUpdateRequest {
    @PositiveOrZero(message = "El precio no puede ser negativo")
    private Double newPrice;
}