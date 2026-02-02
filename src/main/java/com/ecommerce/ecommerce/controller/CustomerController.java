package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dto.AddressDto;
import com.ecommerce.ecommerce.dto.CustomerUpdateRequestDto;
import com.ecommerce.ecommerce.dto.CustomerViewMyProfileDto;
import com.ecommerce.ecommerce.securityConfig.SecurityUtil;
import com.ecommerce.ecommerce.service.CustomerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import java.util.Locale;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.MessageSource;
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
  MessageSource messageSource;

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
      @RequestBody @Valid CustomerUpdateRequestDto customerUpdateRequestDto, Locale locale) {
    // Implementation for viewing customer address goes here
    customerService.updateProfile(customerUpdateRequestDto, securityUtil.getCurrentUserId());
    return ResponseEntity.ok(messageSource.getMessage("profile.updated", null, locale));

  }

  @PatchMapping("/updatePassword")
  public ResponseEntity<String> updatePassword(@Pattern(
          regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%!]).{8,15}$",
          message = "Password must be 8-15 chars with upper, lower, digit & special char"
      )
      @RequestParam String newPassword,
      @RequestParam String confirmPassword,Locale locale) {
    customerService.updatePassword(newPassword, confirmPassword, securityUtil.getCurrentUserId());
    return ResponseEntity.ok(messageSource.getMessage("password.updated", null, locale));

  }


  @PostMapping("/addNewAddress")
  public ResponseEntity<String> addNewAddress(@RequestBody @Valid AddressDto addressDto,Locale locale) {
    customerService.addNewAddress(addressDto, securityUtil.getCurrentUserId());
    return ResponseEntity.ok(messageSource.getMessage("address.added", null, locale));

  }

  @DeleteMapping("/deleteAddress")
  public ResponseEntity<String> deleteAddress(@RequestParam Long addressId,Locale locale) {
    customerService.deleteAddress(addressId, securityUtil.getCurrentUserId());
    return ResponseEntity.ok(messageSource.getMessage("address.deleted", null, locale));

  }

  @PutMapping("/updateAddress")
  public ResponseEntity<String> updateAddress(@RequestBody @Valid AddressDto addressDto,
      @RequestParam Long addressId,Locale locale) {
    customerService.updateAddress(addressId, securityUtil.getCurrentUserId(), addressDto);
    return ResponseEntity.ok(messageSource.getMessage("address.updated", null, locale));

  }


}
