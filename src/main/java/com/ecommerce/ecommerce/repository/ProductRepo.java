package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<ProductEntity,Long> {
    boolean existsByCategory_CategoryId(Long categoryId);

    @Query("""
    SELECT DISTINCT p.brand
    FROM ProductEntity p
    WHERE p.category.categoryId IN :categoryIds
      AND p.isActive = true
      AND p.isDeleted = false
""")
    List<String> findDistinctBrandsByCategoryIds(List<Long> categoryIds);


    boolean existsByProductNameAndBrandAndCategory_CategoryIdAndSeller_User_Id(
            String productName,
            String brand,
            Long categoryId,
            Long sellerId
    );

    boolean existsByProductNameAndBrandAndCategory_CategoryIdAndSeller_User_IdAndProductIdNot(
            String productName,
            String brand,
            Long categoryId,
            Long sellerId,
            Long productId
    );

    Page<ProductEntity> findBySeller_User_Id(Long sellerId,Pageable pageable);


    List<ProductEntity> findByCategory_CategoryId(Long categoryId);

  Page<ProductEntity> findByCategory_CategoryIdInAndIsActiveTrueAndIsDeletedFalse(
      List<Long> categoryIds,
      Pageable pageable
  );

}
