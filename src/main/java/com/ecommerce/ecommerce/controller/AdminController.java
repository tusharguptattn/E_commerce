package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dto.CustomerResponseDto;
import com.ecommerce.ecommerce.dto.SellerResponseDto;
import com.ecommerce.ecommerce.service.AdminService;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Validated
//@PreAuthorize("hasRole('ADMIN')")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminController {

  AdminService adminService;
  static Set<String> ALLOWED_SORTS = Set.of("id", "email");

  @GetMapping("/customers")
  public ResponseEntity<Page<CustomerResponseDto>> getAllRegisteredCustomer(
      @RequestParam(defaultValue = "0") int pageNumber,
      @RequestParam(defaultValue = "10") int pageSize,
      @RequestParam(defaultValue = "id") String sortBy) {

    if (!ALLOWED_SORTS.contains(sortBy)) {
      return ResponseEntity.badRequest().build();
    }

    Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
    return ResponseEntity.ok(adminService.getAllRegisteredCustomers(pageable));
  }

  @GetMapping("/sellers")
  public ResponseEntity<Page<SellerResponseDto>> getAllRegisteredSeller(
      @RequestParam(defaultValue = "0") int pageNumber,
      @RequestParam(defaultValue = "10") int pageSize,
      @RequestParam(defaultValue = "id") String sortBy) {

    if (!ALLOWED_SORTS.contains(sortBy)) {
      return ResponseEntity.badRequest().build();
    }
    Sort sort = sortBy.equals("email")
        ? Sort.by("user.email")
        : Sort.by("sellerId");

    Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
    return ResponseEntity.ok(adminService.getAllRegisteredSeller(pageable));
  }


  @PatchMapping("/{customerId}/activate")
  public void activateCustomerAccount(@PathVariable @NotNull Long customerId) {
    adminService.activateCustomerAccount(customerId);
  }


  @PatchMapping("/{customerId}/deactivate")
  public void deActivateCustomerAccount(@PathVariable @NotNull Long customerId) {
    adminService.deActivateCustomerAccount(customerId);
  }


}
