package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.entity.CategoryMetaDataField;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryMetaDataFieldRepo extends JpaRepository<CategoryMetaDataField,Long> {
    CategoryMetaDataField findByName(String name);
    Page<CategoryMetaDataField> findByNameContainingIgnoreCase(String name, Pageable pageable);

}
