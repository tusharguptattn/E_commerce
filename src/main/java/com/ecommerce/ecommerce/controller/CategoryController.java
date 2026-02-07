package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dto.*;
import com.ecommerce.ecommerce.service.CategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.Locale;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/categories")
@Validated
public class CategoryController {

  CategoryService categoryService;
  MessageSource messageSource;


  @PostMapping("/admin/addMetaData")
  public ResponseEntity<String> addMetaDataField(
      @RequestParam @NotBlank(message = "Field name cannot be blank") String fieldName, Locale locale) {
    categoryService.addMetaDataField(fieldName);
    return ResponseEntity.ok(messageSource.getMessage("metadata.field.added", null, locale));
  }

  @GetMapping("/admin/metadata-fields")
  public Page<CategoryMetadataFieldResponseDto> getAllMetadataFields(
      @RequestParam(defaultValue = "10") int max,
      @RequestParam(defaultValue = "0") int offset,
      @RequestParam(defaultValue = "id") String sort,
      @RequestParam(defaultValue = "asc") String order,
      @RequestParam(required = false) String query
  ) {
    Sort.Direction direction =
        order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

    Pageable pageable = PageRequest.of(offset, max, Sort.by(direction, sort));

    return categoryService.getAllMetadataFields(query, pageable);
  }


  @PostMapping("/admin/addNewCategory")
  public ResponseEntity<String> addCategory(
      @RequestBody @Valid CategoryRequestDto categoryRequestDto,Locale locale) {
    Long categoryId = categoryService.addCategory(categoryRequestDto);

    return ResponseEntity.ok(messageSource.getMessage("category.added", null, locale)+ categoryId);
  }

  @GetMapping("/admin/viewCategory/{id}")
  public ResponseEntity<ViewCategoryResponse> viewCategory(@PathVariable Long id) {
    return ResponseEntity.ok(categoryService.viewCategory(id));
  }


  @GetMapping("/admin/viewAllCategories")
  public Page<ChildCategoryResponseDto> viewAllCategories(
      @RequestParam(defaultValue = "10") int max,
      @RequestParam(defaultValue = "0") int offset,
      @RequestParam(defaultValue = "categoryId") String sort,
      @RequestParam(defaultValue = "asc") String order,
      @RequestParam(required = false) String query
  ) {
    Sort.Direction direction =
        order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
    Pageable pageable = PageRequest.of(offset, max, Sort.by(direction, sort));
    return categoryService.viewAllCategories(query, pageable);
  }


  @PutMapping("/admin/updateCategory")
  public ResponseEntity<String> updateCategory(
      @RequestBody @Valid CategoryUpdateRequestDto categoryRequestDto,Locale locale) {
    categoryService.updateCategory(categoryRequestDto);
    return ResponseEntity.ok(messageSource.getMessage("category.updated", null, locale));


  }

  @PostMapping("/admin/addCategoryMetaDataFieldsValues")
  public ResponseEntity<String> addCategoryMetaDataFieldsValues(
      @RequestBody @Valid CategoryMetaDataFieldValueRequestDto categoryMetaDataFieldRequestDto,Locale locale) {
    categoryService.addMetaDataFieldValues(categoryMetaDataFieldRequestDto);
    return ResponseEntity.ok(messageSource.getMessage("metadata.fieldValue.added", null, locale));
  }

  @PutMapping("/admin/updateCategoryMetaDataFieldsValues")
  public ResponseEntity<String> updateCategoryMetaDataFieldsValues(
      @RequestBody @Valid CategoryMetaDataFieldValueRequestDto categoryMetaDataFieldRequestDto,Locale locale) {
    categoryService.updateMetaDataFieldValues(categoryMetaDataFieldRequestDto);
    return ResponseEntity.ok(messageSource.getMessage("metadata.fieldValue.updated", null, locale));
  }

  // all above for admin


  // for seller
  @GetMapping("/seller/viewAllCategoriesLeaf")
  public ResponseEntity<List<LeafCategoryResponseDto>> viewAllCategories() {
    return ResponseEntity.ok(categoryService.viewAllCategoriesLeaf());
  }

  // for Customer
  @GetMapping("/customer/getAllCategoriesForCustomer")
  public ResponseEntity<List<CategoryMetadataFieldResponseDto>> getAllCategoriesForCustomer(
      @RequestParam(required = false) Long categoryId) {
    return ResponseEntity.ok(categoryService.getAllCategoriesForCustomer(categoryId));
  }

  @GetMapping("/customer/getCategoryFilters")
  public ResponseEntity<CategoryFilterResponseDto> getCategoryFilters(
      @RequestParam Long categoryId) {
    return ResponseEntity.ok(categoryService.getFilteredCategoryData(categoryId));
  }


}
