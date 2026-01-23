package com.ecommerce.ecommerce.controller;


import com.ecommerce.ecommerce.dto.ProductVariationResponseForCustomerDto;
import com.ecommerce.ecommerce.securityConfig.SecurityUtil;
import com.ecommerce.ecommerce.service.CartService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Validated
public class CartController {


  CartService service;
  SecurityUtil securityUtil;


  @PostMapping("/addProductToCart")
  public ResponseEntity<String> addProductToCart(
      @RequestParam @NotNull(message = "Product variation id can not be null") Long productVariationId,
      @RequestParam @Min(value = 1, message = "quantity can not be less than 1") int quantity) {
    service.addProductToCart(productVariationId, quantity);
    return ResponseEntity.ok("Product Successfully added to cart");
  }


  @GetMapping
  public ResponseEntity<List<ProductVariationResponseForCustomerDto>> getCart() {
    return ResponseEntity.ok(service.getCartByUser());
  }


  @DeleteMapping("/remove/{cartItemId}")
  public ResponseEntity<String> removeItem(
      @PathVariable @NotNull(message = "product variation id can not be null") Long productVariationId) {
    service.removeItem(productVariationId);
    return ResponseEntity.ok("Item removed from cart successfully");
  }


  @PutMapping("/update/{cartItemId}")
  public ResponseEntity<String> updateQuantity(
      @RequestParam @NotNull(message = "product variation id can not be null") Long productVariationId,
      @RequestParam @Min(0) int quantity) {
    service.updateQuantity(productVariationId, quantity);
    return ResponseEntity.ok("Cart item quantity updated successfully");
  }


  @DeleteMapping("/clear")
  public ResponseEntity<String> clearCart() {
    service.clearCart();
    return ResponseEntity.ok("Cart cleared successfully");
  }

}
