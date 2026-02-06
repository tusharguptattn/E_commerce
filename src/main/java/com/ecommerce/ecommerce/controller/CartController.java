package com.ecommerce.ecommerce.controller;


import com.ecommerce.ecommerce.dto.ProductVariationResponseForCustomerDto;
import com.ecommerce.ecommerce.securityConfig.SecurityUtil;
import com.ecommerce.ecommerce.service.CartService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Locale;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.MessageSource;
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
  MessageSource messageSource;

  @PostMapping("/addProductToCart")
  public ResponseEntity<String> addProductToCart(
      @RequestParam @NotNull(message = "Product variation id can not be null") Long productVariationId,
      @RequestParam @Min(value = 1, message = "quantity can not be less than 1") int quantity,
      Locale locale) {
    service.addProductToCart(productVariationId, quantity);
    messageSource.getMessage("cart.added", null, locale);
    return ResponseEntity.ok(messageSource.getMessage("cart.added", null, locale));
  }


  @GetMapping
  public ResponseEntity<List<ProductVariationResponseForCustomerDto>> getCart() {
    return ResponseEntity.ok(service.getCartByUser());
  }


  @DeleteMapping("/remove/{productVariationId}")
  public ResponseEntity<String> removeItem(
      @PathVariable @NotNull(message = "product variation id can not be null") Long productVariationId,
      Locale locale) {
    service.removeItem(productVariationId);
    messageSource.getMessage("cart.added", null, locale);
    return ResponseEntity.ok(messageSource.getMessage("remove.cart.item", null, locale));
  }


  @PutMapping("/update/{productVariationId}")
  public ResponseEntity<String> updateQuantity(
      @RequestParam @NotNull(message = "product variation id can not be null") Long productVariationId,
      @RequestParam @Min(0) int quantity, Locale locale) {
    service.updateQuantity(productVariationId, quantity);
    return ResponseEntity.ok(messageSource.getMessage("update.cart.item", null, locale));
  }


  @DeleteMapping("/clear")
  public ResponseEntity<String> clearCart(Locale locale) {
    service.clearCart();
    return ResponseEntity.ok(messageSource.getMessage("cart.clear", null, locale));
  }

}
