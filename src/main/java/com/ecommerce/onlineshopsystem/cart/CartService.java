package com.ecommerce.onlineshopsystem.cart;

import com.ecommerce.onlineshopsystem.cart.dto.AddToCartRequest;
import com.ecommerce.onlineshopsystem.cart.dto.CartResponse;
import com.ecommerce.onlineshopsystem.cartItem.CartItem;
import com.ecommerce.onlineshopsystem.cartItem.CartItemRepository;
import com.ecommerce.onlineshopsystem.cartItem.dto.CartItemResponse;
import com.ecommerce.onlineshopsystem.product.Product;
import com.ecommerce.onlineshopsystem.product.ProductNotFoundException;
import com.ecommerce.onlineshopsystem.product.ProductRepository;
import com.ecommerce.onlineshopsystem.user.User;
import com.ecommerce.onlineshopsystem.user.UserNotFoundException;
import com.ecommerce.onlineshopsystem.user.UserRepository;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;


    public CartService(CartRepository cartRepository,
                       CartItemRepository cartItemRepository,
                       ProductRepository productRepository,
                       UserRepository userRepository) {

        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public CartResponse getCart(String username) {
        return toResponse(getOrCreateCart(username));
    }

    public CartResponse addToCart(String username, AddToCartRequest request) {
        Cart cart = getOrCreateCart(username);

        Product product = productRepository.findById(request.getProductid())
                .orElseThrow(() -> new ProductNotFoundException("" +
                        "Product not found with id " + request.getProductid()));
        Optional<CartItem> existring =
                cartItemRepository.findByCartIdAndProductId(cart.getId() , product.getId());

        int newQuantity = request.getQuantity() + existring.map(CartItem::getQuantity).orElse(0);
        if (newQuantity > product.getStockQuantity()) {
            throw new IllegalArgumentException("Insufficient stock. Available stock: " + product.getStockQuantity());
        }

        if(existring.isPresent()) {
            existring.get().setQuantity(newQuantity);
        } else {
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(newQuantity);
            item.setPriceAtAdd(product.getPrice());
            cart.getItems().add(item);
        }

        return toResponse(cartRepository.save(cart));
    }

    @Transactional
    public CartResponse updateItemQuantity(String username, Long itemId, Integer quantity) {
        if(quantity == null || quantity <=0) {
            throw new IllegalArgumentException("Quantity must positive number");
        }
        Cart cart = getOrCreateCart(username);
        CartItem item = findItemInCart(cart, itemId);

        if(quantity > item.getProduct().getStockQuantity()) {
            throw new IllegalArgumentException(
                    "Insufficient stock. Available stock: " + item.getProduct().getStockQuantity());
        }
        item.setQuantity(quantity);
        return toResponse(cartRepository.save(cart));
    }

    @Transactional
    public CartResponse removeItem(String username, Long itemId) {
        Cart cart = getOrCreateCart(username);
        findItemInCart(cart, itemId);
        cart.getItems().removeIf(i -> i.getId().equals(itemId));
        return toResponse(cartRepository.save(cart));
    }

    @Transactional
    public void clearCart(String username) {
        Cart cart = getOrCreateCart(username);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    private Cart getOrCreateCart(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found" + username));

        return cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    return cartRepository.save(cart);
                });

    }



    private CartItem findItemInCart(Cart cart, Long itemId) {
        return cart.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new CartNotFoundException("Cart item not found with id: " + itemId));
    }

    private CartResponse toResponse(Cart cart) {
        List<CartItemResponse> items = cart.getItems().stream()
                .map(this::toItemResponse)
                .toList();
        BigDecimal total = items.stream()
                .map(CartItemResponse::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        CartResponse response = new CartResponse();
        response.setId(response.getId());
        response.setItems(items);
        response.setTotalAmount(total);
        return response;

    }

    private CartItemResponse toItemResponse(CartItem item) {
        CartItemResponse  response = new CartItemResponse();
        response.setId(item.getId());
        response.setProductId(item.getProduct().getId());
        response.setProductName(item.getProduct().getProductName());
        response.setQuantity(item.getQuantity());
        response.setPriceAtAdd(item.getPriceAtAdd());
        response.setSubtotal(item.getPriceAtAdd().multiply(BigDecimal.valueOf(item.getQuantity())));
        return response;
    }
}
