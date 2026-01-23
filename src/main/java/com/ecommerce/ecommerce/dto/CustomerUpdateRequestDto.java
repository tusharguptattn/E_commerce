package com.ecommerce.ecommerce.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


public record CustomerUpdateRequestDto

        ( // User fields
          @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
          String firstName,

          @Size(max = 50, message = "Middle name must be at most 50 characters")
          String middleName,

          @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
          String lastName,

          // Customer-specific field
          @Pattern(
                  regexp = "^[6-9]\\d{9}$",
                  message = "Invalid phone number"
          )
          String contactNumber) {
}
