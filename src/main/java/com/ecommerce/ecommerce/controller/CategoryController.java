package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dto.*;
import com.ecommerce.ecommerce.service.CategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/categories")
public class CategoryController {

  CategoryService categoryService;


  @PostMapping("/admin/addMetaData")
  public ResponseEntity<String> addMetaDataField(
      @RequestParam @NotBlank(message = "Field name cannot be blank") String fieldName) {
    categoryService.addMetaDataField(fieldName);
    return ResponseEntity.ok("Metadata field added successfully");
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
      @RequestBody @Valid CategoryRequestDto categoryRequestDto) {
    Long categoryId = categoryService.addCategory(categoryRequestDto);
    return ResponseEntity.ok("Category added successfully with ID: " + categoryId);
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
      @RequestBody @Valid CategoryUpdateRequestDto categoryRequestDto) {
    categoryService.updateCategory(categoryRequestDto);
    return ResponseEntity.ok("Category updated successfully");


  }

  @PostMapping("/admin/addCategoryMetaDataFieldsValues")
  public ResponseEntity<String> addCategoryMetaDataFieldsValues(
      @RequestBody @Valid CategoryMetaDataFieldValueRequestDto categoryMetaDataFieldRequestDto) {
    categoryService.addMetaDataFieldValues(categoryMetaDataFieldRequestDto);
    return ResponseEntity.ok("Metadata fields added to category successfully");
  }

  @PutMapping("/admin/updateCategoryMetaDataFieldsValues")
  public ResponseEntity<String> updateCategoryMetaDataFieldsValues(
      @RequestBody @Valid CategoryMetaDataFieldValueRequestDto categoryMetaDataFieldRequestDto) {
    categoryService.updateMetaDataFieldValues(categoryMetaDataFieldRequestDto);
    return ResponseEntity.ok("Metadata fields values updated successfully");
  }

  // al; above for admin


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
