package com.ecommerce.onlineshopsystem.cartItem.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemResponse {

    private Long id;
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal priceAtAdd;
    private BigDecimal subtotal;
}
