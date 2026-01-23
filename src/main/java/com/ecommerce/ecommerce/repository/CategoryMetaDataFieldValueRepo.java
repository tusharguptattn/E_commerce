package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.entity.CategoryMetaDataFieldValues;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryMetaDataFieldValueRepo extends JpaRepository<CategoryMetaDataFieldValues,Long> {
    Optional<CategoryMetaDataFieldValues> findByCategoryEntity_CategoryIdAndCategoryMetaDataField_Id(Long categoryId, Long fieldId);

    //    String findValuesByCategoryMetadataId(Long categoryMetadataId);
    List<CategoryMetaDataFieldValues> findByCategoryEntity_CategoryId(Long categoryId);

    Optional<CategoryMetaDataFieldValues> findByCategoryMetaDataField_Id(Long fieldId);
}
