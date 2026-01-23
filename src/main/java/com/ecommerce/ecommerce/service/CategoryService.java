package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.*;
import com.ecommerce.ecommerce.entity.CategoryEntity;
import com.ecommerce.ecommerce.entity.CategoryMetaDataField;
import com.ecommerce.ecommerce.entity.CategoryMetaDataFieldValues;
import com.ecommerce.ecommerce.exceptionHanding.BadRequest;
import com.ecommerce.ecommerce.repository.*;
import com.ecommerce.ecommerce.securityConfig.SecurityUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {

  CategoryRepo categoryRepo;
  CategoryMetaDataFieldRepo categoryMetaDataFieldRepo;
  ProductRepo productRepo;
  CategoryMetaDataFieldValueRepo categoryMetaDataFieldValueRepo;
  SecurityUtil securityUtil;
  ProductVariationRepo productVariationRepo;


  @Transactional
  public void addMetaDataField(String fieldName) {
    CategoryMetaDataField byName = categoryMetaDataFieldRepo.findByName(fieldName);
    if (byName == null) {
      CategoryMetaDataField categoryMetaDataField = new CategoryMetaDataField();
      categoryMetaDataField.setName(fieldName);
      categoryMetaDataFieldRepo.save(categoryMetaDataField);
    } else {
      throw new BadRequest("Field Already Exists");
    }
  }


  public Page<CategoryMetadataFieldResponseDto> getAllMetadataFields(
      String query,
      Pageable pageable
  ) {
    if (query == null || query.isBlank()) {
      return categoryMetaDataFieldRepo.findAll(pageable).map(
          categoryMetaDataField -> new CategoryMetadataFieldResponseDto(
              categoryMetaDataField.getId(), categoryMetaDataField.getName()));
    }
    return categoryMetaDataFieldRepo.findByNameContainingIgnoreCase(query, pageable)
        .map(categoryMetaDataField -> new CategoryMetadataFieldResponseDto(
            categoryMetaDataField.getId(), categoryMetaDataField.getName()));
  }


  @Transactional
  public Long addCategory(CategoryRequestDto categoryDto) {
    if (categoryDto.parentId() == null) {
      if (categoryRepo.existsByNameAndParentCategoryIsNull(categoryDto.name())) {
        throw new BadRequest("Category name already exists at root level");
      }
      CategoryEntity category = new CategoryEntity();
      category.setName(categoryDto.name());
      CategoryEntity save = categoryRepo.save(category);
      return save.getCategoryId();
    }
    CategoryEntity parentCategory = categoryRepo.findById(categoryDto.parentId())
        .orElseThrow(() -> new BadRequest("Parent category not found"));

    if (productRepo.existsByCategory_CategoryId(parentCategory.getCategoryId())) {
      throw new BadRequest("Cannot add subcategory to a category that has products");
    }

    if (categoryRepo.existsByNameAndParentCategory_CategoryId(categoryDto.name(),
        parentCategory.getCategoryId())) {
      throw new BadRequest("Category name already exists under the specified parent category");
    }

    CategoryEntity childCategory = new CategoryEntity();
    childCategory.setName(categoryDto.name());
    childCategory.setParentCategory(parentCategory);

    CategoryEntity save = categoryRepo.save(childCategory);
    return save.getCategoryId();

  }


  public ViewCategoryResponse viewCategory(Long id) {
    if (id == null) {
      throw new BadRequest("Category ID is required");
    }

    CategoryEntity category = categoryRepo.findById(id)
        .orElseThrow(() -> new BadRequest("Category not found"));

    // Parent Hierarchy

    List<ParentCategoryResponseDto> parentHierarchy = new ArrayList<>();

    CategoryEntity currentParent = category.getParentCategory();

    while (currentParent != null) {
      parentHierarchy.add(
          new ParentCategoryResponseDto(
              currentParent.getCategoryId(),
              currentParent.getName()
          )
      );
      currentParent = currentParent.getParentCategory();
    }

    List<ChildCategoryResponseDto> children =
        categoryRepo.findByParentCategory_CategoryId(id)
            .stream()
            .map(child -> new ChildCategoryResponseDto(
                child.getCategoryId(),
                child.getName()
            ))
            .toList();

    return new ViewCategoryResponse(category.getCategoryId(),
        category.getName(),
        parentHierarchy,
        children);


  }


  public Page<ChildCategoryResponseDto> viewAllCategories(String query, Pageable pageable) {
    if (query == null || query.isBlank()) {
      return categoryRepo.findAll(pageable).map(
          category -> new ChildCategoryResponseDto(category.getCategoryId(), category.getName()));
    } else {
      return categoryRepo.findByNameContainingIgnoreCase(query, pageable)
          .map(category -> new ChildCategoryResponseDto(category.getCategoryId(),
              category.getName()));
    }
  }

  @Transactional
  public void updateCategory(CategoryUpdateRequestDto categoryRequestDto) {
    CategoryEntity category = categoryRepo.findById(categoryRequestDto.id())
        .orElseThrow(() -> new BadRequest("Category not found"));

    if (category.getParentCategory().getCategoryId() == null) {
      if (categoryRepo.existsByNameAndParentCategoryIsNull(categoryRequestDto.name())) {
        throw new BadRequest("Category name already exists at root level");
      }
      category.setName(categoryRequestDto.name());
      categoryRepo.save(category);
    } else {
      if (categoryRepo.existsByNameAndParentCategory_CategoryId(categoryRequestDto.name(),
          category.getParentCategory().getCategoryId())) {
        throw new BadRequest("Category name already exists under the specified parent category");
      }
      category.setName(categoryRequestDto.name());
      categoryRepo.save(category);
    }


  }

  @Transactional
  public void addMetaDataFieldValues(
      CategoryMetaDataFieldValueRequestDto categoryMetaDataFieldValueRequestDto) {

    Set<String> uniqueValues = new HashSet<>(categoryMetaDataFieldValueRequestDto.values());
    if (uniqueValues.size() != categoryMetaDataFieldValueRequestDto.values().size()) {
      throw new BadRequest("Values must be unique for metadata field");
    }

    CategoryEntity category = categoryRepo.findById(
            categoryMetaDataFieldValueRequestDto.categoryId())
        .orElseThrow(() -> new BadRequest("Category not found"));

    CategoryMetaDataField categoryMetaDataField = categoryMetaDataFieldRepo.findById(
            categoryMetaDataFieldValueRequestDto.metaDataFieldId())
        .orElseThrow(() -> new BadRequest("Metadata field not found"));

    CategoryMetaDataFieldValues categoryMetaDataFieldValues = new CategoryMetaDataFieldValues();
    categoryMetaDataFieldValues.setCategoryEntity(category);
    categoryMetaDataFieldValues.setCategoryMetaDataField(categoryMetaDataField);
    categoryMetaDataFieldValues.setValues(
        String.join(",", categoryMetaDataFieldValueRequestDto.values()));
    categoryMetaDataFieldValueRepo.save(categoryMetaDataFieldValues);

  }

  @Transactional
  public void updateMetaDataFieldValues(
      CategoryMetaDataFieldValueRequestDto categoryMetaDataFieldValueRequestDto) {

    // 1️⃣ Validate duplicate values in request (case-insensitive)
    Set<String> newValuesSet = categoryMetaDataFieldValueRequestDto.values().stream()
        .map(String::trim)
        .map(String::toLowerCase)
        .collect(Collectors.toSet());

    if (newValuesSet.size() != categoryMetaDataFieldValueRequestDto.values().size()) {
      throw new BadRequest("Duplicate values in request are not allowed");
    }

    categoryRepo.findById(categoryMetaDataFieldValueRequestDto.categoryId())
        .orElseThrow(() -> new BadRequest("Category not found"));

    CategoryMetaDataFieldValues categoryMetaDataFieldValues = categoryMetaDataFieldValueRepo.findByCategoryEntity_CategoryIdAndCategoryMetaDataField_Id(
        categoryMetaDataFieldValueRequestDto.categoryId(),
        categoryMetaDataFieldValueRequestDto.metaDataFieldId()
    ).orElseThrow(
        () -> new BadRequest("Metadata field values for the specified category not found"));

    Set<String> existingValues = Arrays.stream(categoryMetaDataFieldValues.getValues().split(","))
        .map(String::trim)
        .map(String::toLowerCase)
        .collect(Collectors.toSet());

    for (String value : newValuesSet) {
      if (existingValues.contains(value.toLowerCase())) {
        throw new BadRequest(
            "Value '" + value + "' already exists for this metadata field");
      }
    }

    existingValues.addAll(newValuesSet);
    categoryMetaDataFieldValues.setValues(String.join(",", existingValues));
    categoryMetaDataFieldValueRepo.save(categoryMetaDataFieldValues);
  }


  public List<LeafCategoryResponseDto> viewAllCategoriesLeaf() {
    List<CategoryEntity> leafCategories = categoryRepo.findLeafCategories();
    List<LeafCategoryResponseDto> leafCategoryResponseDtos = new ArrayList<>();

    for (CategoryEntity category : leafCategories) {
      List<CategoryMetadataFieldResponseDto> metadataFieldResponseDtoList = buildParentChain(
          category);
      List<CategoryMetadataResponseDto> metadataFields = new ArrayList<>();
      categoryMetaDataFieldValueRepo.findByCategoryEntity_CategoryId(category.getCategoryId())
          .forEach(entity -> {
            metadataFields.add(
                new CategoryMetadataResponseDto(
                    entity.getCategoryMetaDataField().getName(),
                    Arrays.stream(entity.getValues().split(","))
                        .map(String::trim)
                        .toList()
                )
            );
          });
      leafCategoryResponseDtos.add(
          new LeafCategoryResponseDto(
              category.getCategoryId(),
              category.getName(),
              metadataFieldResponseDtoList,
              metadataFields
          ));

    }

    return leafCategoryResponseDtos;

  }

  public List<CategoryMetadataFieldResponseDto> getAllCategoriesForCustomer(Long categoryId) {
    if (categoryId == null) {
      return categoryRepo.findByParentCategoryIsNull().stream()
          .map(c -> new CategoryMetadataFieldResponseDto(c.getCategoryId(), c.getName())).toList();
    }
    return categoryRepo.findByParentCategory_CategoryId(categoryId).stream()
        .map(c -> new CategoryMetadataFieldResponseDto(c.getCategoryId(), c.getName())).toList();

  }


  public CategoryFilterResponseDto getFilteredCategoryData(Long categoryId) {

    CategoryEntity category = categoryRepo.findById(categoryId)
        .orElseThrow(() -> new BadRequest("Category not found"));

    List<CategoryMetaDataFieldValues> byCategoryCategoryId = categoryMetaDataFieldValueRepo.findByCategoryEntity_CategoryId(
        categoryId);
    List<MetadataFilterDto> metadataFilters = byCategoryCategoryId.stream()
        .map(entity -> new MetadataFilterDto(
            entity.getCategoryMetaDataField().getName(),
            Arrays.stream(entity.getValues().split(","))
                .map(String::trim)
                .toList()
        ))
        .toList();

    // Brands
    List<Long> categoryIds = new ArrayList<>();

    collectSubCategoryIdsIterative(category, categoryIds);

    List<String> brands = productRepo.findDistinctBrandsByCategoryIds(categoryIds);

    // Price Range

    Object[] minMaxPriceByCategoryIds = productVariationRepo.findMinMaxPriceByCategoryIds(
        categoryIds);
    Object[] row = (Object[]) minMaxPriceByCategoryIds[0];
    BigDecimal minPrice = row[0] != null ? (BigDecimal) row[0] : BigDecimal.ZERO;
    BigDecimal maxPrice = row[1] != null ? (BigDecimal) row[1] : BigDecimal.ZERO;
    PriceRangeDto priceRange = new PriceRangeDto(minPrice, maxPrice);

    return new CategoryFilterResponseDto(
        metadataFilters,
        brands,
        priceRange
    );


  }


  private List<CategoryMetadataFieldResponseDto> buildParentChain(CategoryEntity category) {
    List<CategoryMetadataFieldResponseDto> parents = new ArrayList<>();
    CategoryEntity current = category.getParentCategory();

    while (current != null) {
      parents.add(
          new CategoryMetadataFieldResponseDto(current.getCategoryId(), current.getName())
      );
      current = current.getParentCategory();
    }

    return parents;
  }


  public void collectSubCategoryIdsIterative(CategoryEntity root, List<Long> ids) {

    Deque<CategoryEntity> stack = new ArrayDeque<>();
    stack.push(root);

    while (!stack.isEmpty()) {
      CategoryEntity current = stack.pop();
      ids.add(current.getCategoryId());

      List<CategoryEntity> children =
          categoryRepo.findByParentCategory_CategoryId(current.getCategoryId());

      for (CategoryEntity child : children) {
        stack.push(child);
      }
    }
  }


}
