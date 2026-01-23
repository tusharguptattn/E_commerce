package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.entity.OrderStatusEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStatusRepo extends JpaRepository<OrderStatusEntity,Long> {

  Optional<OrderStatusEntity> findTopByOrderProduct_ItemIdOrderByTimestampDesc(Long productId);

}
