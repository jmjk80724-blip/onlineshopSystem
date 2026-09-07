package com.ecommerce.onlineshopsystem.product.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {


   @NotBlank(message = " Product name is required")
    private String productName;

    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;

    @NotNull(message = "Stock quantity is required")
    private Integer stockQuantity;
    private String imageUrl;

    @NotNull(message = "Category ID is required")
    private  Long categoryId;
}
