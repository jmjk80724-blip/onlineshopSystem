package com.ecommerce.onlineshopsystem.cart;

import com.ecommerce.onlineshopsystem.cart.dto.AddToCartRequest;
import com.ecommerce.onlineshopsystem.cart.dto.CartResponse;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart(Authentication authentication) {
        return ResponseEntity.ok(cartService.getCart(authentication.getName()));
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addToCart(Authentication authentication,
                                                  @Valid @RequestBody AddToCartRequest request){
        return ResponseEntity.ok(cartService.addToCart(authentication.getName(), request));
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<CartResponse> updateItem(Authentication authentication,
                                                   @PathVariable Long itemId,
                                                   @RequestParam Integer quantity){
        return ResponseEntity.ok(cartService.updateItemQuantity(authentication.getName(), itemId, quantity));
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<CartResponse> removeItem(Authentication authentication,
                                                   @PathVariable Long itemId){
        return ResponseEntity.ok(cartService.removeItem(authentication.getName(), itemId));
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(Authentication authentication){
        cartService.clearCart(authentication.getName());
        return ResponseEntity.noContent().build();
    }

}

