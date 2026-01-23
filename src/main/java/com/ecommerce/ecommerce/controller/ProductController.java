package com.ecommerce.ecommerce.controller;


import com.ecommerce.ecommerce.dto.*;
import com.ecommerce.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@Validated
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {

  ProductService productService;

  // Seller

  @PostMapping("/addProduct")
  public ResponseEntity<String> addProduct(
      @RequestBody @Valid ProductRequestDto productRequestDto) {
    productService.addProduct(productRequestDto);
    return ResponseEntity.ok("Product added successfully");
  }

  // Seller
  @PostMapping(value = "/addProductVariants", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<String> getProductById(
      @ModelAttribute @Valid ProductVariationRequestDto productVariationRequestDto) {
    productService.addProductVariation(productVariationRequestDto);
    return ResponseEntity.ok("Product Variation added successfully");
  }

  // Seller
  @GetMapping("/{productId}")
  public ResponseEntity<ProductResponseDto> getProductById(
      @PathVariable @NotNull(message = "product id can not be null") Long productId) {
    return ResponseEntity.ok(productService.getProductById(productId));

  }
  // Seller
  @GetMapping("/variant/{variantId}")
  public ResponseEntity<ProductVariationResponseDto> getProductVariationById(
      @PathVariable @NotNull(message = "variant id can not be null") Long variantId) {
    return ResponseEntity.ok(productService.getProductVariationById(variantId));
  }
  // Seller
  @GetMapping("/viewAllProducts")
  public ResponseEntity<Page<ProductResponseDto>> getAllProducts(
      @RequestParam(defaultValue = "10") int max,
      @RequestParam(defaultValue = "0") int offset,
      @RequestParam(defaultValue = "productId") String sort,
      @RequestParam(defaultValue = "asc") String order,
      @RequestParam(required = false) String query) {
    Sort.Direction direction =
        order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
    Sort sortOrder = Sort.by(direction, sort);
    org.springframework.data.domain.Pageable pageable = PageRequest.of(offset, max, sortOrder);
    Page<ProductResponseDto> allProducts = productService.getAllProducts(pageable);
    return ResponseEntity.ok(allProducts);
  }
  // Seller
  @GetMapping("/allVariations/{productId}")
  public ResponseEntity<Page<ProductVariationResponseWithoutProductDetailsDto>> getAllvariationsForProduct(
      @PathVariable @NotNull(message = "product id can not be null") Long productId,
      @RequestParam(defaultValue = "10") int max,
      @RequestParam(defaultValue = "0") int offset,
      @RequestParam(defaultValue = "id") String sort,
      @RequestParam(defaultValue = "asc") String order,
      @RequestParam(required = false) String query) {
    Sort.Direction direction =
        order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
    Sort sortOrder = Sort.by(direction, sort);
    org.springframework.data.domain.Pageable pageable = PageRequest.of(offset, max, sortOrder);
    return ResponseEntity.ok(productService.getAllVariationsForProduct(productId, pageable));

  }
  // Seller
  @DeleteMapping("/delete/{productId}")
  public ResponseEntity<String> deleteProduct(
      @PathVariable @NotNull(message = "product id can not be null") Long productId) {

    productService.deleteProduct(productId);
    return ResponseEntity.ok("Product deleted successfully");
  }

  // Seller
  @PutMapping("/update")
  public ResponseEntity<String> updateProduct(
      @RequestBody @Valid ProductUpdateRequestDto productUpdateRequestDto) {

    productService.updateProduct(productUpdateRequestDto);
    return ResponseEntity.ok("Product Updated Successfully");
  }
  // Seller
  @PutMapping("/updateProductVariation")
  public void updateProductVariation(
      @RequestBody @Valid ProductVariationUpdateRequest productVariationUpdateRequest) {

    productService.updateProductVariationData(productVariationUpdateRequest);

  }

  // Customer

  // for admin also

  @GetMapping("/ViewProductByIdCustomer/{productId}")
  public ResponseEntity<ProductResponseForCustomer> viewProductById(
      @PathVariable @NotNull(message = "Product id can not be null") Long productId) {
    return ResponseEntity.ok(productService.getProductByIdForCustomer(
        productId));
  }

  // customer

  // admin


  @GetMapping("/ViewAllProductByIdCustomer/{categoryId}")
  public ResponseEntity<Page<ProductResponseForCustomer>> getAllProductOfaCategory(
      @PathVariable @NotNull(message = "Category id can not be null") Long categoryId,
      @RequestParam(defaultValue = "10") int max,
      @RequestParam(defaultValue = "0") int offset,
      @RequestParam(defaultValue = "productId") String sort,
      @RequestParam(defaultValue = "asc") String order,
      @RequestParam(required = false) String query) {

    Sort.Direction direction = order.equalsIgnoreCase("desc")? Direction.DESC : Direction.ASC;

    Sort sortOrder = Sort.by(direction,sort);

    Pageable pageable = PageRequest.of(offset,max,sortOrder);

    return ResponseEntity.ok(productService.getAllProductForCategory(
        categoryId, query, pageable));


  }

  // customer

  @GetMapping("ViewAllSimilarProducts/{categoryId}")
  public ResponseEntity<Page<ProductResponseForCustomer>> getAllSimilarProducts( @PathVariable @NotNull(message = "Category id can not be null") Long productId,
      @RequestParam(defaultValue = "10") int max,
      @RequestParam(defaultValue = "0") int offset,
      @RequestParam(defaultValue = "productId") String sort,
      @RequestParam(defaultValue = "asc") String order,
      @RequestParam(required = false) String query){

    Sort.Direction direction = order.equalsIgnoreCase("desc")? Direction.DESC : Direction.ASC;

    Sort sortOrder = Sort.by(direction,sort);

    Pageable pageable = PageRequest.of(offset,max,sortOrder);

    return ResponseEntity.ok(productService.getAllSimilarProduct(
        productId, query, pageable));


  }


  // admin

  @PutMapping("/deactivateProduct/{productId}")
  public ResponseEntity<String> deactivateProduct(@PathVariable @NotNull(message = "product id can not be null") Long productId){
    productService.deactivateProductByAdmin(productId);
    return ResponseEntity.ok("Product Deactivated");
  }


  // admin

  @PutMapping("/activateProduct/{productId}")
  public ResponseEntity<String> activateProduct(@PathVariable @NotNull(message = "product id can not be null") Long productId){
    productService.activateProductByAdmin(productId);
    return ResponseEntity.ok("Product Activated");
  }






}
