package com.ecommerce.onlineshopsystem.cartItem;


import com.ecommerce.onlineshopsystem.cart.Cart;
import com.ecommerce.onlineshopsystem.product.Product;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@Entity
@Table (name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false, unique = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Cart cart;

    @ManyToOne
    @JoinColumn( name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal priceAtAdd;
}
