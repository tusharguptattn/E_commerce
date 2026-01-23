package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.entity.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepo extends JpaRepository<CategoryEntity, Long> {
    boolean existsByNameAndParentCategoryIsNull(String name);

    List<CategoryEntity> findByParentCategoryIsNull();


    boolean existsByNameAndParentCategory_CategoryId(
            String name, Long parentId
    );

    List<CategoryEntity> findByParentCategory_CategoryId(Long parentCategoryId);

    boolean existsByParentCategory_CategoryId(Long categoryId);

    Page<CategoryEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("""
                SELECT c FROM CategoryEntity c
                WHERE c.categoryId NOT IN (
                    SELECT DISTINCT pc.parentCategory.categoryId
                    FROM CategoryEntity pc
                    WHERE pc.parentCategory IS NOT NULL
                )
            """)
    List<CategoryEntity> findLeafCategories();


}
