package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dto.CartItemRequestDto;
import com.ecommerce.ecommerce.dto.CartResponseDto;
import com.ecommerce.ecommerce.securityConfig.SecurityUtil;
import com.ecommerce.ecommerce.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {


    private final CartService service;
    private SecurityUtil securityUtil;

    public CartController(CartService service, SecurityUtil securityUtil) {
        this.service = service;
        this.securityUtil = securityUtil;
    }


    @PostMapping("/add")
    public ResponseEntity<CartResponseDto> addProductToCart(@RequestBody @Valid CartItemRequestDto dto){
        return ResponseEntity.ok(service.addProductToCart(dto,securityUtil.getCurrentUserId()));
    }

    @PutMapping("/update/{cartItemId}")
    public ResponseEntity<CartResponseDto> updateQuantity(@PathVariable Long cartItemId, @RequestParam int quantity)  {
        return ResponseEntity.ok(service.updateQuantity(cartItemId, quantity,securityUtil.getCurrentUserId()));
    }

    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<CartResponseDto> removeItem(@PathVariable Long cartItemId) {
        return ResponseEntity.ok(service.removeItem(cartItemId,securityUtil.getCurrentUserId()));
    }

    @GetMapping
    public ResponseEntity<CartResponseDto> getCart()  {
        return ResponseEntity.ok(service.getCartByUser(securityUtil.getCurrentUserId()));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart() {
        boolean b = service.clearCart(securityUtil.getCurrentUserId());
        if(b){
            return ResponseEntity.noContent().build();
        }else {
            return ResponseEntity.notFound().build();
        }
    }

}
