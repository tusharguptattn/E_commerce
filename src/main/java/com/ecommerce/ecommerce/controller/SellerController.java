package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dto.CompanyAddressCreateRequestDto;
import com.ecommerce.ecommerce.dto.CompanyAddressRequestDto;
import com.ecommerce.ecommerce.dto.SellerProfileUpdateRequestDto;
import com.ecommerce.ecommerce.dto.SellerViewMyProfileDto;
import com.ecommerce.ecommerce.dto.UpdatePasswordForSellerDto;
import com.ecommerce.ecommerce.securityConfig.SecurityUtil;
import com.ecommerce.ecommerce.service.SellerService;
import jakarta.validation.Valid;
import java.util.Locale;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sellers")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SellerController {

  SellerService sellerService;
  SecurityUtil securityUtil;
  MessageSource messageSource;


  @GetMapping("/viewMyProfile")
  public ResponseEntity<SellerViewMyProfileDto> viewMyProfile() {

    return ResponseEntity.ok(sellerService.getProfile(securityUtil.getCurrentUserId()));
  }

  @PutMapping("/updateProfile")
  public ResponseEntity<String> updateProfile(@RequestBody @Valid SellerProfileUpdateRequestDto sellerProfileUpdateRequestDto,
      Locale locale) {
    sellerService.updateProfile(sellerProfileUpdateRequestDto,securityUtil.getCurrentUserId());
    return ResponseEntity.ok(messageSource.getMessage("seller.profile.updated",null,locale));
  }

  @PatchMapping("/updatePassword")
  public ResponseEntity<String> updatePassword(@RequestBody @Valid UpdatePasswordForSellerDto updatePasswordForSellerDto,Locale locale) {
    sellerService.updatePassword(updatePasswordForSellerDto,securityUtil.getCurrentUserId());
    return ResponseEntity.ok(messageSource.getMessage("seller.password.updated",null,locale));

  }

  @PostMapping("/company-address")
  public ResponseEntity<String> addAddress(@RequestBody @Valid CompanyAddressCreateRequestDto companyAddressRequestDto,
      Locale locale) {
    sellerService.addAddress(companyAddressRequestDto, securityUtil.getCurrentUserId());
    return ResponseEntity.ok(messageSource.getMessage("address.added", null, locale));
  }

  @PutMapping("/updateAddress")
  public ResponseEntity<String> updateAddress(@RequestBody @Valid CompanyAddressRequestDto companyAddressRequestDto,Locale locale) {
    sellerService.updateAdderess(companyAddressRequestDto,securityUtil.getCurrentUserId());
    return ResponseEntity.ok(messageSource.getMessage("address.updated",null,locale));
  }


}
