package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepo extends JpaRepository<OrderEntity,Long> {

    Page<OrderEntity> findByUser_Id(Long userId, Pageable pageable);

}
