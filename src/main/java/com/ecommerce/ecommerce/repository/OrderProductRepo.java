package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.entity.OrderProduct;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepo extends JpaRepository<OrderProduct,Long> {
  List<OrderProduct> findAllByOrder_OrderId(Long orderId);
  List<OrderProduct> findAllByOrder_OrderIdIn(List<Long> orderIds);
  Page<OrderProduct> findByProduct_Product_Seller_User_Id(Long userId, Pageable pageable);
}
