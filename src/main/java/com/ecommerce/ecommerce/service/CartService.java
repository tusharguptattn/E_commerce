package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.CartItemRequestDto;
import com.ecommerce.ecommerce.dto.CartItemResponseDto;
import com.ecommerce.ecommerce.dto.CartResponseDto;
import com.ecommerce.ecommerce.entity.CartEntity;
import com.ecommerce.ecommerce.entity.CartItemEntity;
import com.ecommerce.ecommerce.entity.ProductEntity;
import com.ecommerce.ecommerce.entity.UserEntity;
import com.ecommerce.ecommerce.exceptionHanding.ResourceNotFoundException;
import com.ecommerce.ecommerce.repository.CartItemRepo;
import com.ecommerce.ecommerce.repository.CartRepo;
import com.ecommerce.ecommerce.repository.ProductRepo;
import com.ecommerce.ecommerce.repository.UserRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartService {

    private final CartRepo cartRepo;
    private final UserRepo userRepo;
    private final ProductRepo productRepo;
    private final CartItemRepo cartItemRepo;

    public CartService(UserRepo userRepo, ProductRepo productRepo,
                       CartRepo cartRepo, CartItemRepo cartItemRepo) {
        this.userRepo = userRepo;
        this.productRepo = productRepo;
        this.cartRepo = cartRepo;
        this.cartItemRepo = cartItemRepo;
    }
    @Transactional
    public CartResponseDto addProductToCart(CartItemRequestDto dto,Long userId) {
        UserEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        CartEntity cart = user.getCart();
        ProductEntity product = productRepo.findById(dto.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (dto.quantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }


        // check if product already exists in cart
        CartItemEntity existing = cartItemRepo.findByCartIdAndProductId(cart.getCartId(), product.getProductId());
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + dto.quantity());
            cartItemRepo.save(existing);
        } else {
            CartItemEntity item = new CartItemEntity();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(dto.quantity());
            cartItemRepo.save(item);
        }

        return buildCartResponse(cart);
    }
    @Transactional
    public CartResponseDto updateQuantity(Long cartItemId, int quantity,Long userId)  {

        CartItemEntity item = cartItemRepo.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
        if(!item.getCart().getUser().getId().equals(userId)){
            throw new ResourceNotFoundException("Cart item not found for this user");
        }
        item.setQuantity(quantity);
        cartItemRepo.save(item);

        return buildCartResponse(item.getCart());
    }
    @Transactional
    public CartResponseDto removeItem(Long cartItemId,Long userId)  {
        CartItemEntity item = cartItemRepo.findById(cartItemId).orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
        if (!item.getCart().getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Cart item not found for user");
        }

        CartEntity cart = item.getCart();
        cartItemRepo.delete(item);

        return buildCartResponse(cart);
    }

    public CartResponseDto getCartByUser(Long userId) {
        UserEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return buildCartResponse(user.getCart());
    }
    @Transactional
    public Boolean clearCart(Long userId) {
        CartEntity cart = cartRepo.findByUser_Id(userId);
        if (cart == null) return false;
        cart.getCartItems().clear();
        return true;
    }

    private CartResponseDto buildCartResponse(CartEntity cart) {
        List<CartItemResponseDto> items = cart.getCartItems().stream()
                .map(ci -> new CartItemResponseDto(
                        ci.getCartItemId(),
                        ci.getProduct().getProductId(),
                        ci.getProduct().getProductName(),
                        ci.getProduct().getPrice(),
                        ci.getQuantity()
                )).toList();

        double total = items.stream().mapToDouble(i -> i.price() * i.quantity()).sum();

        return new CartResponseDto(cart.getCartId(), items, total);
    }
}


