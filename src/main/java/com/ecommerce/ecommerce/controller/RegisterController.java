package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dto.SellerRequestDto;
import com.ecommerce.ecommerce.dto.UserRequestDto;
import com.ecommerce.ecommerce.securityConfig.SecurityUtil;
import com.ecommerce.ecommerce.service.RegisterService;
import jakarta.validation.Valid;
import java.util.Locale;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/register")
@RequiredArgsConstructor
public class RegisterController {

  RegisterService registerService;
  SecurityUtil securityUtil;
  MessageSource messageSource;

  @PostMapping
  public ResponseEntity<String> registerUser(@RequestBody @Valid UserRequestDto userRequestDto,
      Locale locale) {
    // Registration logic goes here
    registerService.registerUser(userRequestDto);
    return ResponseEntity.ok(messageSource.getMessage("welcome.message", null, locale));
  }

  @GetMapping("/activate")
  public ResponseEntity<String> activateAccount(@RequestParam String token, Locale locale) {
    if (registerService.activateUser(token)) {
      return ResponseEntity.ok(messageSource.getMessage("account.activated",null,locale));
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
      @RequestBody @Valid SellerRequestDto userRequestDto,Locale locale) {
    registerService.registerSeller(userRequestDto);
    return ResponseEntity.ok(messageSource.getMessage("welcome.message", null, locale));

  }


}




