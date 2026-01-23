package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.entity.ProductVariation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductVariationRepo extends JpaRepository<ProductVariation,Long> {

    @Query("""
    SELECT MIN(pv.price), MAX(pv.price)
    FROM ProductVariation pv
    JOIN pv.product p
    WHERE p.category.categoryId IN :categoryIds
      AND p.isActive = true
      AND p.isDeleted = false
      AND pv.isActive = true
""")
    Object[] findMinMaxPriceByCategoryIds(List<Long> categoryIds);

    Page<ProductVariation> findByProduct_ProductId(Long producttId, Pageable pageable);

    List<ProductVariation> findByProduct_ProductId(Long productId);

    Page<ProductVariation> findAllByProduct_ProductIdIn(List<Long> productId,Pageable pageable);


}
