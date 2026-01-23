package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dto.SellerRequestDto;
import com.ecommerce.ecommerce.dto.UserRequestDto;
import com.ecommerce.ecommerce.securityConfig.SecurityUtil;
import com.ecommerce.ecommerce.service.RegisterService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/register")
public class RegisterController {

  RegisterService registerService;
  SecurityUtil securityUtil;

  public RegisterController(RegisterService registerService, SecurityUtil securityUtil) {
    this.registerService = registerService;
    this.securityUtil = securityUtil;
  }


  @PostMapping("/register")
  public ResponseEntity<String> registerUser(@RequestBody @Valid UserRequestDto userRequestDto) {
    // Registration logic goes here
    registerService.registerUser(userRequestDto);
    return ResponseEntity.ok("User registered successfully");
  }

  @GetMapping("/activate")
  public ResponseEntity<String> activateAccount(@RequestParam String token) {
    if (registerService.activateUser(token)) {
      return ResponseEntity.ok("Account activated successfully");
    }
    return ResponseEntity.badRequest()
        .body("Activation token expired. A new activation email has been sent.");
  }

  @PostMapping("/resend-activation")
  public ResponseEntity<String> resendActivationEmail(@RequestParam String email) {
    registerService.resendActivationEmail(email);
    return ResponseEntity.ok("A new activation email has been sent.");

  }

  @PostMapping("/register-seller")
  public ResponseEntity<String> registerSeller(
      @RequestBody @Valid SellerRequestDto userRequestDto) {
    registerService.registerSeller(userRequestDto);
    return ResponseEntity.ok("Seller registered successfully");

  }


}




