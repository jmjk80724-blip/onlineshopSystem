package com.ecommerce.onlineshopsystem.cart.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AddToCartRequest {

    @NotNull(message = "Product ID is Request")
    private Long productid;

    @NotNull(message = "Quantity is Request")
    @Positive(message = "Quantity must be positive")
    private Integer quantity;
}
