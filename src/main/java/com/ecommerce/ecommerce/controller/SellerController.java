package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dto.CompanyAddressRequestDto;
import com.ecommerce.ecommerce.dto.ResetPasswordRequestDto;
import com.ecommerce.ecommerce.dto.SellerProfileUpdateRequestDto;
import com.ecommerce.ecommerce.dto.SellerViewMyProfileDto;
import com.ecommerce.ecommerce.dto.UpdatePasswordForSellerDto;
import com.ecommerce.ecommerce.securityConfig.SecurityUtil;
import com.ecommerce.ecommerce.service.SellerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sellers")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SellerController {

  SellerService sellerService;
  SecurityUtil securityUtil;


  @GetMapping("/viewMyProfile")
  public ResponseEntity<SellerViewMyProfileDto> viewMyProfile() {

    return ResponseEntity.ok(sellerService.getProfile(securityUtil.getCurrentUserId()));
  }

  @PatchMapping("/updateProfile")
  public ResponseEntity<String> updateProfile(@RequestBody @Valid SellerProfileUpdateRequestDto sellerProfileUpdateRequestDto){
    sellerService.updateProfile(sellerProfileUpdateRequestDto,securityUtil.getCurrentUserId());
    return ResponseEntity.ok("Seller Profile Updated Successfully");
  }

  @PatchMapping("/updatePassword")
  public ResponseEntity<String> updatePassword(@RequestBody @Valid UpdatePasswordForSellerDto updatePasswordForSellerDto){
    sellerService.updatePassword(updatePasswordForSellerDto,securityUtil.getCurrentUserId());
    return ResponseEntity.ok("Password Updated Successfully");

  }

  public ResponseEntity<String> updateAddress(@RequestBody @Valid CompanyAddressRequestDto companyAddressRequestDto) {
    sellerService.updateAdderess(companyAddressRequestDto,securityUtil.getCurrentUserId());
    return ResponseEntity.ok("Address Updated Successfully");
  }


}
