package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dto.ProductRequestDto;
import com.ecommerce.ecommerce.dto.ProductResponseDto;
import com.ecommerce.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private ProductService productService;
    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> addProduct(@RequestBody @Valid ProductRequestDto productRequestDto){
        return ResponseEntity.ok(productService.addProduct(productRequestDto));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable  Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductRequestDto productRequestDto) {
        return ResponseEntity.ok(productService.updateProduct(id, productRequestDto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        if(productService.deleteProduct(id)){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }


    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(@RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "5") int size){
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ProductResponseDto> allProducts = productService.getAllProducts(pageRequest);
        return ResponseEntity.ok(allProducts);
    }


    @GetMapping("/category/{category_Name}")
    public ResponseEntity<Page<ProductResponseDto>> getProductByCategory(@PathVariable("category_Name") String categoryName, @RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "5") int size)  {

        PageRequest pageRequest = PageRequest.of(page, size);
        return ResponseEntity.ok(productService.getProductsByCategory(categoryName,pageRequest));
    }


}
