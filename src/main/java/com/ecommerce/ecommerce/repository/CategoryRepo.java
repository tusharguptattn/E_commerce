package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepo extends JpaRepository<CategoryEntity,Long> {
    Optional<CategoryEntity> findCategoryByName(String categoryName);
}
