package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.ProductRequestDto;
import com.ecommerce.ecommerce.dto.ProductResponseDto;
import com.ecommerce.ecommerce.entity.CategoryEntity;
import com.ecommerce.ecommerce.entity.ProductEntity;
import com.ecommerce.ecommerce.exceptionHanding.ResourceNotFoundException;
import com.ecommerce.ecommerce.repository.CategoryRepo;
import com.ecommerce.ecommerce.repository.ProductRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class ProductService {

    private ProductRepo productRepo;

    private CategoryRepo categoryRepo;

    public ProductService(ProductRepo productRepo, CategoryRepo categoryRepo){
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
    }
    @Transactional
    public ProductResponseDto addProduct(ProductRequestDto productRequestDto) throws RuntimeException{

        CategoryEntity categoryFound = categoryRepo.findCategoryByName(productRequestDto.category()).orElseThrow(() -> new RuntimeException("Category Not Found"));
        ProductEntity product = new ProductEntity();
        product.setProductName(productRequestDto.name());
        product.setDescription(productRequestDto.description());
        product.setPrice(productRequestDto.price());
        product.setStockPresent(productRequestDto.stockPresent());
        product.setCategory(categoryFound);
        product.setCreatedAt(new Date(System.currentTimeMillis()));
        product.setUpdatedAt(new Date(System.currentTimeMillis()));
        ProductEntity save = productRepo.save(product);
        return mapToDto(save);
    }



    public ProductResponseDto getProductById(Long id)  {
        ProductEntity productEntity = productRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not Found By Id"));
        return mapToDto(productEntity);
    }

    @Transactional
    public ProductResponseDto updateProduct(Long id , ProductRequestDto productRequestDto)  {

        CategoryEntity category = categoryRepo.findCategoryByName(productRequestDto.category()).orElseThrow(() -> new ResourceNotFoundException("Category Not Found"));
        ProductEntity productEntity = productRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not Found By Id"));
        productEntity.setProductName(productRequestDto.name());
        productEntity.setDescription(productRequestDto.description());
        productEntity.setPrice(productRequestDto.price());
        productEntity.setStockPresent(productRequestDto.stockPresent());
        productEntity.setCategory(category);
        ProductEntity save = productRepo.save(productEntity);
        return mapToDto(save);
    }


    public boolean deleteProduct(Long id ){
        if(!productRepo.existsById(id)){
            return false;
        }
        productRepo.deleteById(id);
        return true;
    }


    public Page<ProductResponseDto> getAllProducts(Pageable pageable){
        Page<ProductEntity> all = productRepo.findAll(pageable);
        return all.map(product -> new ProductResponseDto(product.getProductId(),product.getProductName(), product.getDescription(), product.getPrice(), product.getStockPresent(), product.getCategory().getName()));
    }


    public Page<ProductResponseDto> getProductsByCategory(String  category_Name,Pageable pageable)  {
        categoryRepo.findCategoryByName(category_Name).orElseThrow(()->new ResourceNotFoundException("Category not Found"));
         return productRepo.findByCategory_Name(category_Name,pageable).map(this::mapToDto);
    }








    private ProductResponseDto mapToDto(ProductEntity p){
        return new ProductResponseDto(
                p.getProductId(),
                p.getProductName(),
                p.getDescription(),
                p.getPrice(),
                p.getStockPresent(),
                p.getCategory().getName()
        );
    }
}

