package com.ecommerce.onlineshopsystem.product.dto;



import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductResponse {

    private Long id;
    private String productName;
    private String description;

    private BigDecimal price;

    private Integer stockQuantity;
    private String imageUrl;

    private String CategoryName;
    private LocalDateTime createdAt;
}
