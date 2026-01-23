package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dto.LoginRequest;
import com.ecommerce.ecommerce.dto.LoginResponseDto;
import com.ecommerce.ecommerce.dto.ResetPasswordRequestDto;
import com.ecommerce.ecommerce.service.LoginLogoutService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Validated
public class LoginController {

  LoginLogoutService loginLogoutService;


  @PostMapping("/login")
  public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequest dto) {
    return ResponseEntity.ok(loginLogoutService.login(dto));
  }

  @PostMapping("/logout")
  public ResponseEntity<String> logoutUser(HttpServletRequest request) {
    String authHeader = request.getHeader("Authorization");
    String logout = loginLogoutService.logout(authHeader);

    return ResponseEntity.ok(logout);
  }


  @PostMapping("/forgot-password")
  public ResponseEntity<String> forgotPassword(
      @RequestParam @Email(message = "Email Nor Valid") String email) {
    return ResponseEntity.ok(loginLogoutService.forgotPassword(email));

  }

  @PutMapping("/reset-password")
  public void resetPassword(@RequestParam String token,
      @RequestBody @Valid ResetPasswordRequestDto resetPasswordRequestDto) {
    loginLogoutService.resetPassword(token, resetPasswordRequestDto);
  }
}
