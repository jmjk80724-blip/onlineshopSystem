package com.ecommerce.onlineshopsystem.product;

import com.ecommerce.onlineshopsystem.category.Category;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column( nullable = false)
    private String productName;
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column( nullable = false)
    private BigDecimal price;
    @Column( nullable = false)
    private Integer stockQuantity;
    private String imageUrl;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
