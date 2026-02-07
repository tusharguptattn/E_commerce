package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.CategoryMetadataFieldResponseDto;
import com.ecommerce.ecommerce.dto.ChildCategoryResponseDto;
import com.ecommerce.ecommerce.dto.ProductRequestDto;
import com.ecommerce.ecommerce.dto.ProductResponseDto;
import com.ecommerce.ecommerce.dto.ProductResponseForCustomer;
import com.ecommerce.ecommerce.dto.ProductUpdateRequestDto;
import com.ecommerce.ecommerce.dto.ProductVariationRequestDto;
import com.ecommerce.ecommerce.dto.ProductVariationResponseDto;
import com.ecommerce.ecommerce.dto.ProductVariationResponseForCustomerDto;
import com.ecommerce.ecommerce.dto.ProductVariationResponseWithoutProductDetailsDto;
import com.ecommerce.ecommerce.dto.ProductVariationUpdateRequest;
import com.ecommerce.ecommerce.dto.UserResponseDto;
import com.ecommerce.ecommerce.entity.CategoryEntity;
import com.ecommerce.ecommerce.entity.CategoryMetaDataField;
import com.ecommerce.ecommerce.entity.CategoryMetaDataFieldValues;
import com.ecommerce.ecommerce.entity.ProductEntity;
import com.ecommerce.ecommerce.entity.ProductVariation;
import com.ecommerce.ecommerce.entity.SellerEntity;
import com.ecommerce.ecommerce.exceptionHanding.BadRequest;
import com.ecommerce.ecommerce.exceptionHanding.UnauthorizedException;
import com.ecommerce.ecommerce.repository.CategoryMetaDataFieldRepo;
import com.ecommerce.ecommerce.repository.CategoryMetaDataFieldValueRepo;
import com.ecommerce.ecommerce.repository.CategoryRepo;
import com.ecommerce.ecommerce.repository.ProductRepo;
import com.ecommerce.ecommerce.repository.ProductVariationRepo;
import com.ecommerce.ecommerce.repository.SellerRepo;
import com.ecommerce.ecommerce.repository.UserRepo;
import com.ecommerce.ecommerce.securityConfig.SecurityUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService {

  ProductRepo productRepo;

  CategoryRepo categoryRepo;

  SecurityUtil securityUtil;
  SellerRepo sellerRepo;

  CategoryMetaDataFieldRepo categoryMetaDataFieldRepo;
  CategoryMetaDataFieldValueRepo categoryMetaDataFieldValueRepo;

  ProductVariationRepo productVariationRepo;
  CategoryService categoryService;
  EmailService emailService;
  UserRepo userRepo;


  @Transactional
  public void addProduct(ProductRequestDto productRequestDto) throws RuntimeException {
    CategoryEntity category = categoryRepo.findById(productRequestDto.catgegoryId())
        .orElseThrow(() -> new BadRequest("Category Not Found"));
    SellerEntity seller = sellerRepo.findByUser_Id(securityUtil.getCurrentUserId())
        .orElseThrow(() -> new BadRequest("Seller Not Found"));

    if (categoryRepo.existsByParentCategory_CategoryId(productRequestDto.catgegoryId())) {
      throw new BadRequest("Cannot add product to a non-leaf category");
    }
    if (productRepo.existsByProductNameAndBrandAndCategory_CategoryIdAndSeller_User_Id(
        productRequestDto.name(), productRequestDto.brand(), productRequestDto.catgegoryId(),
        securityUtil.getCurrentUserId())) {
      throw new BadRequest(
          "Product with the same name and brand already exists for this seller in the specified category");
    }

    ProductEntity product = new ProductEntity();
    product.setProductName(productRequestDto.name());
    product.setBrand(productRequestDto.brand());
    product.setCategory(category);
    product.setDescription(productRequestDto.description());
    product.setCancellable(productRequestDto.isCancellabele());
    product.setReturnable(productRequestDto.isReturnable());
    product.setSeller(seller);

    ProductEntity savedProduct = productRepo.save(product);

    Page<UserResponseDto> admin = userRepo.findByRoles_Authority("ADMIN", Pageable.unpaged());

    for (String s : admin.stream().map(u -> u.email()).toList()) {
      emailService.sendProductActivationMail(savedProduct,s);
    }



  }


  @Transactional
  public void addProductVariation(ProductVariationRequestDto productVariationRequestDto) {

    Map<String, Object> metadata = null;

    if (productVariationRequestDto.metadata() != null) {
      ObjectMapper objectMapper = new ObjectMapper();
      try {
        metadata = objectMapper.readValue(productVariationRequestDto.metadata(),
            new TypeReference<Map<String, Object>>() {
            });
      } catch (Exception e) {
        throw new RuntimeException("Invalid metadata JSON");
      }
    }

    // validating product existence
    ProductEntity productEntity = productRepo.findById(productVariationRequestDto.productId())
        .orElseThrow(() -> new BadRequest("Product not Found By Id"));

    // validating product is active and not deleted
    if (!productEntity.isActive() || productEntity.isDeleted()) {
      throw new BadRequest("Cannot add variations to an inactive or deleted product");
    }

    //
    for (Map.Entry<String, Object> entry : metadata.entrySet()) {
      String key = entry.getKey();
      Object value = entry.getValue();
      CategoryMetaDataField byName = categoryMetaDataFieldRepo.findByName(key);
      if (byName == null) {
        throw new BadRequest("Invalid metadata field: " + key);
      }

      CategoryMetaDataFieldValues categoryMetaDataFieldValues = categoryMetaDataFieldValueRepo.findByCategoryEntity_CategoryIdAndCategoryMetaDataField_Id(
          productEntity.getCategory().getCategoryId(), byName.getId()).orElseThrow(
          () -> new BadRequest("Metadata field not valid for the product's category: " + key));
      Set<String> allowedValues = Arrays.stream(categoryMetaDataFieldValues.getValues().split(","))
          .map(String::trim).map(String::toLowerCase).collect(Collectors.toSet());
      if (!allowedValues.contains(value.toString())) {
        throw new BadRequest("Invalid value for metadata field " + key + ": " + value);
      }


    }

    Page<ProductVariation> byProductProductId = productVariationRepo.findByProduct_ProductId(
        productEntity.getProductId(), Pageable.unpaged());

    // checking json structure of metadata should be same as existing one

    ProductVariation existingVariations = byProductProductId.stream().findFirst().orElse(null);

    if (existingVariations != null) {
      Map<String, Object> existingMetadata = existingVariations.getMetadata();
      if (!existingMetadata.keySet().equals(metadata.keySet())) {
        throw new BadRequest("All variations must have the same metadata structure");
      }
    }

    try {
      BufferedImage bufferedImage = ImageIO.read(
          productVariationRequestDto.primaryImage().getInputStream());
    } catch (IOException e) {
      throw new BadRequest("Invalid image file");
    }

    MultipartFile image = productVariationRequestDto.primaryImage();

    if (image.isEmpty()) {
      throw new BadRequest("Primary image is required for product variation");
    }

    if (image.getContentType() == null || !image.getContentType().startsWith("image/")) {
      throw new BadRequest("Invalid image format");
    }
    String originalFileName = image.getOriginalFilename();

    ProductVariation productVariation = new ProductVariation();
    productVariation.setProduct(productEntity);
    productVariation.setQuantityAvailable(productVariationRequestDto.quantityAvailable());
    productVariation.setPrice(productVariationRequestDto.price());
    productVariation.setMetadata(metadata);
    productVariation.setPrimaryImageName(originalFileName);
    productVariation.setActive(true);

    productVariationRepo.save(productVariation);


  }


  public ProductResponseDto getProductById(Long id) {
    ProductEntity productEntity = productRepo.findById(id)
        .orElseThrow(() -> new BadRequest("Product not Found By Id"));
    if (!productEntity.getSeller().getUser().getId().equals(securityUtil.getCurrentUserId())) {
      throw new BadRequest("Unauthorized access to product");
    }
    if (productEntity.isDeleted()) {
      throw new BadRequest("Product is deleted");
    }

    return new ProductResponseDto(productEntity.getProductId(), productEntity.getProductName(),
        productEntity.getBrand(),
        new ChildCategoryResponseDto(productEntity.getCategory().getCategoryId(),
            productEntity.getCategory().getName()), productEntity.getDescription());

  }

  public ProductVariationResponseDto getProductVariationById(Long id) {
    ProductVariation productVariation = productVariationRepo.findById(id)
        .orElseThrow(() -> new BadRequest("Product Variation not Found By Id"));
    if (!productVariation.getProduct().getSeller().getUser().getId()
        .equals(securityUtil.getCurrentUserId())) {
      throw new BadRequest("Unauthorized access to product variation");
    }

    if (productVariation.getProduct().isDeleted()) {
      throw new BadRequest("Product is deleted");
    }

    return new ProductVariationResponseDto(productVariation.getId(), productVariation.getMetadata(),
        productVariation.getQuantityAvailable(), productVariation.getPrice(),
        new ProductResponseDto(productVariation.getProduct().getProductId(),
            productVariation.getProduct().getProductName(),
            productVariation.getProduct().getBrand(), new ChildCategoryResponseDto(
            productVariation.getProduct().getCategory().getCategoryId(),
            productVariation.getProduct().getCategory().getName()),
            productVariation.getProduct().getDescription()));

  }


  public Page<ProductResponseDto> getAllProducts(Pageable pageable) {
    Page<ProductEntity> bySellerUserId = productRepo.findBySeller_User_Id(
        securityUtil.getCurrentUserId(), pageable);
    return bySellerUserId.map(productEntity -> new ProductResponseDto(productEntity.getProductId(),
        productEntity.getProductName(), productEntity.getBrand(),
        new ChildCategoryResponseDto(productEntity.getCategory().getCategoryId(),
            productEntity.getCategory().getName()), productEntity.getDescription()));
  }


  public Page<ProductVariationResponseWithoutProductDetailsDto> getAllVariationsForProduct(
      Long productId, Pageable pageable) {
    ProductEntity productEntity = productRepo.findById(productId)
        .orElseThrow(() -> new BadRequest("Product not Found By Id"));
    if (!productEntity.getSeller().getUser().getId().equals(securityUtil.getCurrentUserId())) {
      throw new BadRequest("Unauthorized access to product");
    }
    if (productEntity.isDeleted()) {
      throw new BadRequest("Product is deleted");
    }

    Page<ProductVariation> byProductProductId = productVariationRepo.findByProduct_ProductId(
        productId, pageable);

    return byProductProductId.map(
        productVariations -> new ProductVariationResponseWithoutProductDetailsDto(
            productVariations.getId(), productVariations.getMetadata(),
            productVariations.getQuantityAvailable(), productVariations.getPrice()));
  }

  @Transactional
  public void deleteProduct(Long id) {
    ProductEntity productEntity = productRepo.findById(id)
        .orElseThrow(() -> new BadRequest("Product not Found By Id"));
    if (!productEntity.getSeller().getUser().getId().equals(securityUtil.getCurrentUserId())) {
      throw new BadRequest("Unauthorized access to product");
    }
    if (productEntity.isDeleted()) {
      throw new BadRequest("Product is already deleted");
    }

    productEntity.setDeleted(true);
    productEntity.setActive(false);
    productRepo.save(productEntity);
  }


  @Transactional
  public void updateProduct(ProductUpdateRequestDto productUpdateRequestDto) {
    ProductEntity productEntity = productRepo.findById(productUpdateRequestDto.ProductId())
        .orElseThrow(() -> new BadRequest("No Product found with this id"));
    if (!productEntity.getSeller().getUser().getId().equals(securityUtil.getCurrentUserId())) {
      throw new BadRequest("Unauthorized access of product");
    }

    if (productRepo.existsByProductNameAndBrandAndCategory_CategoryIdAndSeller_User_IdAndProductIdNot(
        productUpdateRequestDto.name(), productEntity.getBrand(),
        productEntity.getCategory().getCategoryId(), securityUtil.getCurrentUserId(),
        productEntity.getProductId())) {
      throw new BadRequest("Please give unique name");
    }

    if (productUpdateRequestDto.name() != null) {
      productEntity.setProductName(productUpdateRequestDto.name());
    }
    if (productUpdateRequestDto.desription() != null) {
      productEntity.setDescription(productUpdateRequestDto.desription());
    }
    productEntity.setCancellable(productUpdateRequestDto.isCancellable());
    productEntity.setReturnable(productUpdateRequestDto.isReturnable());
    productRepo.save(productEntity);

  }

  @Transactional
  public void updateProductVariationData(
      ProductVariationUpdateRequest productVariationUpdateRequest) {

    ProductVariation productVariation = productVariationRepo.findById(
            productVariationUpdateRequest.productVariationId())
        .orElseThrow(() -> new BadRequest("No Product Variation found with this id"));

    if (!productVariation.getProduct().getSeller().getUser().getId()
        .equals(securityUtil.getCurrentUserId())) {
      throw new UnauthorizedException("Product Variation update is not accessible");
    }

    if (productVariation.getProduct().isDeleted() || !productVariation.getProduct().isActive()) {
      throw new BadRequest("Product is already deleted or in active");
    }
    Map<String, Object> metadata = null;
    if (productVariationUpdateRequest.metadata() != null
        && !productVariationUpdateRequest.metadata().isBlank()) {
      ObjectMapper objectMapper = new ObjectMapper();
      try {
        metadata = objectMapper.readValue(productVariationUpdateRequest.metadata(),
            new TypeReference<Map<String, Object>>() {});
      } catch (Exception e) {
        throw new BadRequest("Invalid metadata JSON");
      }
    }

    Long categopryId = productVariation.getProduct().getCategory().getCategoryId();
    if (metadata != null) {
      for (Map.Entry<String, Object> m : metadata.entrySet()) {

        CategoryMetaDataField byName = categoryMetaDataFieldRepo.findByName(m.getKey());

        if (byName == null) {
          throw new BadRequest("no meta data found with " + m.getKey());
        }

        CategoryMetaDataFieldValues categoryMetaDataFieldValues = categoryMetaDataFieldValueRepo.findByCategoryEntity_CategoryIdAndCategoryMetaDataField_Id(
                categopryId, byName.getId())
            .orElseThrow(() -> new BadRequest("Not Value associated with " + m.getKey()));

        Set<String> allowedValues = Arrays.stream(categoryMetaDataFieldValues.getValues().split(","))
            .map(String::trim)
            .map(String::toLowerCase)
            .collect(Collectors.toSet());

        String incomingValue = m.getValue().toString().trim().toLowerCase();

        if (!allowedValues.contains(incomingValue)) {
          throw new BadRequest("given metadata value is not present");
        }


      }
    }

    // -------- Optional updates --------
    if (productVariationUpdateRequest.quantity() != null) {
      productVariation.setQuantityAvailable(productVariationUpdateRequest.quantity());
    }
    if (productVariationUpdateRequest.price() != null) {
      productVariation.setPrice(productVariationUpdateRequest.price());
    }

    if (productVariationUpdateRequest.isActive() != null) {
      productVariation.setActive(productVariationUpdateRequest.isActive());
    }

    if (metadata != null) {
      productVariation.setMetadata(metadata);
    }
    if (productVariationUpdateRequest.primaryImage() != null
        && !productVariationUpdateRequest.primaryImage().isEmpty()) {
      productVariation.setPrimaryImageName(
          productVariationUpdateRequest.primaryImage().getOriginalFilename());
    }

    productVariationRepo.save(productVariation);


  }


  public ProductResponseForCustomer getProductByIdForCustomer(Long productId) {
    ProductEntity productEntity = productRepo.findById(productId)
        .orElseThrow(() -> new BadRequest("product id is invalid"));

    if (!productEntity.isActive() || productEntity.isDeleted()) {
      throw new BadRequest("product is inactive or deleted");
    }

    Page<ProductVariation> byProductProductId = productVariationRepo.findByProduct_ProductId(
        productId, Pageable.unpaged());

    if (byProductProductId.isEmpty()) {
      throw new BadRequest("Product do not have any variations");
    }

    List<ProductVariationResponseForCustomerDto> list = byProductProductId.map(
        productVariation -> new ProductVariationResponseForCustomerDto(productVariation.getId(),
            productVariation.getQuantityAvailable(), productVariation.getPrice(),
            productVariation.getMetadata(), productVariation.getPrimaryImageName(),
            productVariation.isActive(),productVariation.getQuantityAvailable()<=0)).stream().toList();

    ChildCategoryResponseDto categoryResponseDto = new ChildCategoryResponseDto(
        productEntity.getCategory().getCategoryId(), productEntity.getCategory().getName());

    return new ProductResponseForCustomer(productEntity.getProductName(), productEntity.getBrand(),
        productEntity.getDescription(), categoryResponseDto, list);

  }


  public Page<ProductResponseForCustomer> getAllProductForCategory(Long categoryId, String query,
      Pageable pageable) {

    CategoryEntity categoryEntity = categoryRepo.findById(categoryId)
        .orElseThrow(() -> new BadRequest("Category not found"));

    //️ Collect category IDs (leaf OR all child categories)
    List<Long> categoryIds = new ArrayList<>();
    categoryService.collectSubCategoryIdsIterative(categoryEntity, categoryIds);

    //  Fetch products (active + not deleted)
    Page<ProductEntity> productPage =
        productRepo.findByCategory_CategoryIdInAndIsActiveTrueAndIsDeletedFalse(
            categoryIds, pageable);

    return productPage.map(product -> {

      List<ProductVariation> variations = productVariationRepo.findByProduct_ProductId(
          product.getProductId());
      if (variations.isEmpty()) {
        return null;
      }
      List<ProductVariationResponseForCustomerDto> variationDtoList = variations.stream()
          .map(var -> new ProductVariationResponseForCustomerDto(var.getId(),
              var.getQuantityAvailable(),
              var.getPrice(),
              var.getMetadata(),
              var.getPrimaryImageName(),
              var.isActive(),
              var.getQuantityAvailable()<=0)).toList();

      return new ProductResponseForCustomer(product.getProductName(), product.getBrand(),
          product.getDescription(),
          new ChildCategoryResponseDto(product.getCategory().getCategoryId(),
              product.getCategory().getName()), variationDtoList);


    });
  }


  public Page<ProductResponseForCustomer> getAllSimilarProduct(Long productId, String query,
      Pageable pageable) {
    ProductEntity productEntity = productRepo.findById(productId)
        .orElseThrow(() -> new BadRequest("Product id is not valid"));

    return getAllProductForCategory(productEntity.getCategory().getCategoryId(), query, pageable);
  }


  public void deactivateProductByAdmin(Long productId) {
    ProductEntity productEntity = productRepo.findById(productId)
        .orElseThrow(() -> new BadRequest("Product not found with this id"));
    if (!productEntity.isActive()) {
      throw new BadRequest("Product not active");

    }

    productEntity.setActive(false);
    productRepo.save(productEntity);

    emailService.sendDeactivationProductEmail(productEntity);
  }


  public void activateProductByAdmin(Long productId) {
    ProductEntity productEntity = productRepo.findById(productId)
        .orElseThrow(() -> new BadRequest("Product not found with this id"));
    if (productEntity.isActive()) {
      throw new BadRequest("Product is already active");

    }

    productEntity.setActive(true);
    productRepo.save(productEntity);
    emailService.sendActivationProductEmail(productEntity);


  }


}
