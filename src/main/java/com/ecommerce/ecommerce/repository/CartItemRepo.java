package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.entity.CartItemEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepo extends JpaRepository<CartItemEntity,Long> {

  Optional<CartItemEntity> findByCart_IdAndProductVariation_Id(Long productVariationId,Long cartId);

}
