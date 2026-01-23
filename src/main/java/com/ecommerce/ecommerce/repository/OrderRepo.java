package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.entity.OrderEntity;
import com.ecommerce.ecommerce.entity.OrderProduct;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepo extends JpaRepository<OrderEntity,Long> {

    Page<OrderEntity> findByCustomer_User_Id(Long userId, Pageable pageable);



}
