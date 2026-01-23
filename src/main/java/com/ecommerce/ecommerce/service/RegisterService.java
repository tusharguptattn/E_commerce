package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.SellerRequestDto;
import com.ecommerce.ecommerce.dto.UserRequestDto;
import com.ecommerce.ecommerce.entity.*;
import com.ecommerce.ecommerce.exceptionHanding.BadRequest;
import com.ecommerce.ecommerce.repository.*;

import com.ecommerce.ecommerce.securityConfig.JwtService;
import com.ecommerce.ecommerce.utility.TokenGeneration;
import jakarta.validation.ValidationException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RegisterService {

  UserRepo userRepo;
  JwtService jwtService;
  PasswordEncoder passwordEncoder;
  AuthenticationManager authenticationManager;
  CustomerRepo customerRepo;
  RoleRepo roleRepo;
  UserActivationTokenRepo userActivationTokenRepo;
  EmailService emailService;
  TokenGeneration tokenGeneration;
  SellerRepo sellerRepo;


  @Transactional
  public void registerUser(UserRequestDto userRequestDto) {
    // Registration logic goes here

    if (!userRequestDto.password().equals(userRequestDto.confirmPassword())) {
      throw new ValidationException("Passwords do not match");
    }

    if (userRepo.existsByEmail(userRequestDto.email())) {
      throw new BadRequest("User with given email already exists");
    }
    UserEntity userEntity = new UserEntity();
    userEntity.setFirstName(userRequestDto.firstName());
    userEntity.setLastName(userRequestDto.lastName());
    userEntity.setMiddleName(userRequestDto.middleName());
    userEntity.setEmail(userRequestDto.email());
    userEntity.setPassword(passwordEncoder.encode(userRequestDto.password()));

    RoleEntity customerRole = roleRepo.findByAuthority("ROLE_CUSTOMER");
    userEntity.getRoles().add(customerRole);
    userEntity.setActive(false);

    UserEntity savedUser = userRepo.save(userEntity);

    CustomerEntity customerEntity = new CustomerEntity();
    customerEntity.setContactNumber(userRequestDto.phoneNumber());
    customerEntity.setUser(userEntity);
    customerRepo.save(customerEntity);

    emailService.sendActivationEmail(userRequestDto.email(),
        tokenGeneration.generateToken(savedUser).getToken(), savedUser.getFirstName());


  }


  @Transactional
  public boolean activateUser(String token) {
    UserActivationToken activationToken = userActivationTokenRepo.findByToken(token)
        .orElseThrow(() -> new BadRequest("Invalid activation token"));
    if (activationToken.isUsed()) {
      throw new BadRequest("Token already used");
    }
    if (activationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
      emailService.sendActivationEmail(activationToken.getUser().getEmail(),
          tokenGeneration.reGenerateToken(activationToken).getToken(),
          activationToken.getUser().getFirstName());
      return false;
    }

    UserEntity user = activationToken.getUser();
    user.setActive(true);
    activationToken.setUsed(true);

    userRepo.save(user);
    userActivationTokenRepo.delete(activationToken);
    return true;

  }

  @Transactional
  public void resendActivationEmail(String email) {
    UserEntity user = userRepo.findByEmail(email)
        .orElseThrow(() -> new BadRequest("User with given email does not exist"));
    if (user.isActive()) {
      throw new BadRequest("Account is already activated");
    }
    Optional<UserActivationToken> existingTokenOpt = userActivationTokenRepo.findByUser_Id(
        user.getId());
    UserActivationToken token;
    if (existingTokenOpt.isPresent()) {
      token = tokenGeneration.reGenerateToken(existingTokenOpt.get());
    } else {
      token = tokenGeneration.generateToken(user);
    }
    emailService.sendActivationEmail(email, token.getToken(), user.getFirstName());
  }

  @Transactional
  public void registerSeller(SellerRequestDto sellerRequestDto) {
    // Registration logic goes here

    if (!sellerRequestDto.password().equals(sellerRequestDto.confirmPassword())) {
      throw new ValidationException("Passwords do not match");
    }

    if (userRepo.existsByEmail(sellerRequestDto.email())) {
      throw new BadRequest("User with given email already exists");
    }

    UserEntity userEntity = new UserEntity();
    userEntity.setFirstName(sellerRequestDto.firstName());
    userEntity.setLastName(sellerRequestDto.lastName());
    userEntity.setMiddleName(sellerRequestDto.middleName());
    userEntity.setEmail(sellerRequestDto.email());
    userEntity.setPassword(passwordEncoder.encode(sellerRequestDto.password()));
    RoleEntity sellerRole = roleRepo.findByAuthority("ROLE_SELLER");
    userEntity.getRoles().add(sellerRole);
    userEntity.setActive(false);

    userRepo.save(userEntity);

    SellerEntity sellerEntity = new SellerEntity();
    sellerEntity.setBusinessName(sellerRequestDto.companyName());
    sellerEntity.setCompanyContactNumber(sellerRequestDto.companyContactNumber());
    sellerEntity.setUser(userEntity);
    sellerRepo.save(sellerEntity);

    emailService.sendWaitForApprovalEmail(sellerRequestDto.email());

  }

}
