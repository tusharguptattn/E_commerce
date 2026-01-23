package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.ProductVariationResponseForCustomerDto;
import com.ecommerce.ecommerce.entity.CartEntity;
import com.ecommerce.ecommerce.entity.CartItemEntity;
import com.ecommerce.ecommerce.entity.CustomerEntity;
import com.ecommerce.ecommerce.entity.ProductEntity;
import com.ecommerce.ecommerce.entity.ProductVariation;
import com.ecommerce.ecommerce.exceptionHanding.BadRequest;
import com.ecommerce.ecommerce.repository.CartItemRepo;
import com.ecommerce.ecommerce.repository.CartRepo;
import com.ecommerce.ecommerce.repository.CustomerRepo;
import com.ecommerce.ecommerce.repository.ProductRepo;
import com.ecommerce.ecommerce.repository.ProductVariationRepo;
import com.ecommerce.ecommerce.repository.UserRepo;
import com.ecommerce.ecommerce.securityConfig.SecurityUtil;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CartService {

  CartRepo cartRepo;
  UserRepo userRepo;
  ProductRepo productRepo;
  SecurityUtil securityUtil;
  CustomerRepo customerRepo;
  ProductVariationRepo productVariationRepo;
  CartItemRepo cartItemRepo;

  @Transactional
  public void addProductToCart(Long productVariationId, int quantity) {

    ProductVariation productVariation = productVariationRepo.findById(productVariationId)
        .orElseThrow(() -> new BadRequest("Product Variation not found"));

    if (!productVariation.isActive() || productVariation.getQuantityAvailable() <= 0) {
      throw new BadRequest("Product is out of stock");
    }

    Long userId = securityUtil.getCurrentUserId();

    CustomerEntity customerEntity = customerRepo.findByUserId(userId)
        .orElseThrow(() -> new BadRequest("Not able to find a valid customer"));

    CartEntity cart = cartRepo.findByCustomer_User_Id(userId);

    if (cart == null) {
      cart = new CartEntity();
      cart.setCustomer(customerEntity);
      cartRepo.save(cart);
    }

    Optional<CartItemEntity> existingItem = cartItemRepo.findByCart_IdAndProductVariation_Id(
        cart.getId(),
        productVariationId);

    if (existingItem.isPresent()) {
      CartItemEntity cartItem = existingItem.get();
      int newQuantity = cartItem.getQuantity() + quantity;

      if (newQuantity > productVariation.getQuantityAvailable()) {
        throw new BadRequest("Requested quantity exceeds available stock");
      }

      cartItem.setQuantity(newQuantity);
      cartItemRepo.save(cartItem);
    } else {
      if (quantity > productVariation.getQuantityAvailable()) {
        throw new BadRequest("Requested quantity exceeds available stock");
      }
      CartItemEntity cartItem = new CartItemEntity();
      cartItem.setProductVariation(productVariation);
      cartItem.setQuantity(quantity);
      cartItem.setCart(cart);
      cartItem.setInWishlist(false);

      cartItemRepo.save(cartItem);
    }

  }


  public List<ProductVariationResponseForCustomerDto> getCartByUser() {

    CartEntity byCustomerUserId = cartRepo.findByCustomer_User_Id(securityUtil.getCurrentUserId());

    if (byCustomerUserId == null) {
      return List.of();
    }

    List<CartItemEntity> items = byCustomerUserId.getItems();

    return items.stream().map(cartItem -> {

      ProductVariation pv = cartItem.getProductVariation();

      boolean outOfStock = !pv.isActive() || pv.getQuantityAvailable() < cartItem.getQuantity();

      return new ProductVariationResponseForCustomerDto(pv.getId(), cartItem.getQuantity(),
          pv.getPrice(), pv.getMetadata(), pv.getPrimaryImageName(), pv.isActive(), outOfStock);

    }).toList();

  }


  @Transactional
  public void removeItem(Long productVariationId) {
    CartEntity byCustomerUserId = cartRepo.findByCustomer_User_Id(securityUtil.getCurrentUserId());

    if (byCustomerUserId == null) {
      throw new BadRequest("Cart not found");
    }
    CartItemEntity item = cartItemRepo.findByCart_IdAndProductVariation_Id(byCustomerUserId.getId(),
        productVariationId).orElseThrow(() -> new BadRequest("Cart item not found"));

    cartItemRepo.delete(item);
  }


  @Transactional
  public void updateQuantity(Long productVariationId, int quantity) {

    ProductVariation productVariation = productVariationRepo.findById(productVariationId)
        .orElseThrow(() -> new BadRequest("Not product found with this id"));

    ProductEntity productEntity = productRepo.findById(productVariation.getProduct().getProductId())
        .orElseThrow(() -> new BadRequest("Product not found with variation id"));

    if (!productVariation.isActive() || productEntity.isDeleted()) {
      throw new BadRequest("Product is deactivated or deleted");
    }

    CartEntity byCustomerUserId = cartRepo.findByCustomer_User_Id(securityUtil.getCurrentUserId());

    if (byCustomerUserId == null) {
      throw new BadRequest("Cart not available");
    }

    CartItemEntity cartItem1 = byCustomerUserId.getItems().stream()
        .filter(cartItem -> cartItem.getProductVariation().getId().equals(productVariationId))
        .findFirst().orElseThrow(() -> new BadRequest("Product not exist in car"));

    if (quantity == 0) {
      cartItemRepo.delete(cartItem1);
      return;
    }

    int newQuantity = cartItem1.getQuantity() + quantity;

    if (newQuantity > productVariation.getQuantityAvailable()) {
      throw new BadRequest("Required quantity is not available");
    }

    cartItem1.setQuantity(newQuantity);

    cartItemRepo.save(cartItem1);

  }

  @Transactional
  public void clearCart() {
    CartEntity cart = cartRepo.findByCustomer_User_Id(securityUtil.getCurrentUserId());

    if (cart == null) {
      throw new BadRequest("Cart not found");
    }


    cartRepo.delete(cart);

  }
}


