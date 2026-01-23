package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dto.AddressDto;
import com.ecommerce.ecommerce.dto.CustomerUpdateRequestDto;
import com.ecommerce.ecommerce.dto.CustomerViewMyProfileDto;
import com.ecommerce.ecommerce.securityConfig.SecurityUtil;
import com.ecommerce.ecommerce.service.CustomerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/customers")
public class CustomerController {

  CustomerService customerService;
  SecurityUtil securityUtil;

  @GetMapping("/viewMyProfile")
  public ResponseEntity<CustomerViewMyProfileDto> viewMyProfile() {
    // Implementation for customer profile viewing goes here
    return ResponseEntity.ok(customerService.getProfile(securityUtil.getCurrentUserId()));
  }

  @GetMapping("/viewAddress")
  public ResponseEntity<List<AddressDto>> viewAddress() {
    // Implementation for viewing customer address goes here
    return ResponseEntity.ok(customerService.viewAddress(securityUtil.getCurrentUserId()));
  }

  @PatchMapping("/updateProfile")
  public ResponseEntity<String> updateProfile(
      @RequestBody @Valid CustomerUpdateRequestDto customerUpdateRequestDto) {
    // Implementation for viewing customer address goes here
    customerService.updateProfile(customerUpdateRequestDto, securityUtil.getCurrentUserId());
    return ResponseEntity.ok("Profile updated successfully");

  }

  @PatchMapping("/updatePassword")
  public ResponseEntity<String> updatePassword(@Pattern(
          regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%!]).{8,15}$",
          message = "Password must be 8-15 chars with upper, lower, digit & special char"
      )
      @RequestParam String newPassword,
      @RequestParam String confirmPassword) {
    customerService.updatePassword(newPassword, confirmPassword, securityUtil.getCurrentUserId());
    return ResponseEntity.ok("Password updated successfully");

  }


  @PostMapping("/addNewAddress")
  public ResponseEntity<String> addNewAddress(@RequestBody @Valid AddressDto addressDto) {
    customerService.addNewAddress(addressDto, securityUtil.getCurrentUserId());
    return ResponseEntity.ok("Address added successfully");

  }

  @DeleteMapping("/deleteAddress")
  public ResponseEntity<String> deleteAddress(@RequestParam Long addressId) {
    customerService.deleteAddress(addressId, securityUtil.getCurrentUserId());
    return ResponseEntity.ok("Address deleted successfully");

  }

  @PutMapping("/updateAddress")
  public ResponseEntity<String> updateAddress(@RequestBody @Valid AddressDto addressDto,
      @RequestParam Long addressId) {
    customerService.updateAddress(addressId, securityUtil.getCurrentUserId(), addressDto);
    return ResponseEntity.ok("Address updated successfully");

  }


}
