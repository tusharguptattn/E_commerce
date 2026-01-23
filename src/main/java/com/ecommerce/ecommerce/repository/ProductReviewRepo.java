package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.entity.ProductReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductReviewRepo extends JpaRepository<ProductReviewEntity,Long> {
}
