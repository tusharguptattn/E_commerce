package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.entity.SellerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellerRepo extends JpaRepository<SellerEntity,Long> {
    Optional<SellerEntity> findByUser_Id(Long userId);
}
