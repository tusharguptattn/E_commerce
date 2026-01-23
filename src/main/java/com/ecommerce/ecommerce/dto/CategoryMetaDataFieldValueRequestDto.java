package com.ecommerce.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CategoryMetaDataFieldValueRequestDto(
        @NotNull(message = "Category ID cannot be null")
        Long categoryId,

        @NotNull(message = "MetaData Field ID cannot be null")
        Long metaDataFieldId,

        @NotEmpty(message = "Values list cannot be empty")
        List<@NotBlank(message = "Value can not be blank") String> values) {
}
