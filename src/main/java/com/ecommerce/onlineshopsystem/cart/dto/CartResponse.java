package com.ecommerce.onlineshopsystem.cart.dto;

import com.ecommerce.onlineshopsystem.cartItem.dto.CartItemResponse;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CartResponse {
    private Long id;
    private List<CartItemResponse> items;
    private BigDecimal totalAmount;
}
