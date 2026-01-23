package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.LoginRequest;
import com.ecommerce.ecommerce.dto.LoginResponseDto;
import com.ecommerce.ecommerce.dto.ResetPasswordRequestDto;
import com.ecommerce.ecommerce.entity.BlacklistedToken;
import com.ecommerce.ecommerce.entity.PasswordResetToken;
import com.ecommerce.ecommerce.entity.UserEntity;
import com.ecommerce.ecommerce.exceptionHanding.BadRequest;
import com.ecommerce.ecommerce.repository.BlacklistedTokenRepo;
import com.ecommerce.ecommerce.repository.PasswordResetTokenRepo;
import com.ecommerce.ecommerce.repository.UserRepo;
import com.ecommerce.ecommerce.securityConfig.CustomUserDetails;
import com.ecommerce.ecommerce.securityConfig.JwtService;
import com.ecommerce.ecommerce.utility.TokenGeneration;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LoginLogoutService {


  UserRepo userRepo;
  JwtService jwtService;
  AuthenticationManager authenticationManager;
  EmailService emailService;
  BlacklistedTokenRepo blacklistedTokenRepo;
  TokenGeneration tokenGeneration;
  PasswordResetTokenRepo passwordResetTokenRepo;
  PasswordEncoder passwordEncoder;


  public LoginResponseDto login(LoginRequest dto) {

    UserEntity user = userRepo.findByEmail(dto.email())
        .orElseThrow(() -> new BadRequest("Invalid email or password"));

    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              dto.email(), dto.password()
          )
      );
      user.setInvalidAttemptCount(0);
      userRepo.save(user);
      return new LoginResponseDto(user.getId(),
          jwtService.generateToken(new CustomUserDetails(user)));
    } catch (BadCredentialsException e) {
      int invalidAttemptCount = user.getInvalidAttemptCount() + 1;
      user.setInvalidAttemptCount(invalidAttemptCount);
      if (invalidAttemptCount >= 3) {
        user.setLocked(true);
      }
      userRepo.save(user);
      if (invalidAttemptCount >= 3) {
        emailService.sendAccountLockedEmail(user.getEmail());
      }
      throw new BadRequest(invalidAttemptCount >= 3 ? "Account locked after 3 failed attempts"
          : "Invalid email or password");
    }
  }


  @Transactional
  public String logout(String authHeader) {
    String token = null;
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      token = authHeader.substring(7);
    }

    if (blacklistedTokenRepo.existsByToken(token)) {
      return "User already logged out"; // already logged out, idempotent
    }

    Date date = jwtService.extractExpiration(token);

    if (date.before(new Date())) {
      return "Token Expired"; // token already expired, no need to blacklist
    }

    BlacklistedToken blacklistedToken = new BlacklistedToken();
    blacklistedToken.setToken(token);
    blacklistedToken.setExpiresAt(date);
    blacklistedTokenRepo.save(blacklistedToken);
    return "Logout Successful";
  }


  public String forgotPassword(String email) {
    UserEntity user = userRepo.findByEmail(email)
        .orElseThrow(() -> new BadRequest("User not found"));

    if (!user.isActive()) {
      throw new BadRequest("User is not active");
    }

    if (passwordResetTokenRepo.existsByUser_Id(user.getId())) {
      PasswordResetToken existingToken = passwordResetTokenRepo.findByUser_Id(user.getId());
      passwordResetTokenRepo.delete(existingToken);
    }

    PasswordResetToken passwordResetToken = tokenGeneration.generatePasswordResetToken(user);

    emailService.sendResetPasswordEmail(email, passwordResetToken.getToken());

    return "Password reset email sent";

  }

  public void resetPassword(String token, ResetPasswordRequestDto resetPasswordRequestDto) {
    PasswordResetToken resetToken = passwordResetTokenRepo.findByToken(token)
        .orElseThrow(() -> new BadRequest("Invalid password reset token"));

    if (resetToken.getExpiryTime().isBefore(LocalDateTime.now())) {
      throw new BadRequest("Password reset token has expired");
    }

    if (!resetPasswordRequestDto.newPassword().equals(resetPasswordRequestDto.confirmPassword())) {
      throw new BadRequest("Password and Confirm Password do not match");
    }
    UserEntity user = resetToken.getUser();
    user.setPassword(passwordEncoder.encode(
        resetPasswordRequestDto.newPassword())); // In real application, encode the password
    user.setPasswordUpdatedDate(LocalDateTime.now());
    userRepo.save(user);

    passwordResetTokenRepo.delete(resetToken);
  }


}
