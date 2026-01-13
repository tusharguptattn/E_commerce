package com.ecommerce.ecommerce.dto;


import jakarta.validation.constraints.*;

public record ProductRequestDto(
        @NotBlank
        @Size(min = 3, max = 100, message = "Product name must be between 3 and 100 characters")
        String name,

        @NotBlank(message = "Description is required")
        @Size(min = 10,max=200,message = "Description must be between 10 and 200 characters")
        String description,

        @NotNull(message = "Price is required")
        @Positive(message = "Price must be greater than 0")
        Double price ,

        @Min(value = 0, message = "Stock cannot be negative")
        int stockPresent,

        @NotBlank(message = "Category is required")
        @Size(min = 3, max = 50, message = "Category must be between 3 and 50 characters")
        String category) {
}
