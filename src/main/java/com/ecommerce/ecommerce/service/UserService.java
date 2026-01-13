package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.LoginRequest;
import com.ecommerce.ecommerce.dto.LoginResponseDto;
import com.ecommerce.ecommerce.dto.UserRequestDto;
import com.ecommerce.ecommerce.dto.UserResponseDto;
import com.ecommerce.ecommerce.entity.BlacklistedToken;
import com.ecommerce.ecommerce.entity.CartEntity;
import com.ecommerce.ecommerce.entity.UserEntity;
import com.ecommerce.ecommerce.exceptionHanding.UnauthorizedException;
import com.ecommerce.ecommerce.exceptionHanding.UserNotFound;
import com.ecommerce.ecommerce.repository.BlacklistedTokenRepo;
import com.ecommerce.ecommerce.repository.UserRepo;

import com.ecommerce.ecommerce.securityConfig.CustomUserDetails;
import com.ecommerce.ecommerce.securityConfig.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserService {
    private UserRepo userRepo;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final BlacklistedTokenRepo blacklistedTokenRepo;


    public UserService(UserRepo repo, JwtService jwtService, PasswordEncoder passwordEncoder, BlacklistedTokenRepo blacklistedTokenRepo) {
        this.userRepo = repo;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.blacklistedTokenRepo = blacklistedTokenRepo;
    }


    @Transactional
    public UserResponseDto registerUser(UserRequestDto userRequestDto) {
        // Registration logic goes here
        UserEntity userEntity =  new UserEntity();
        userEntity.setName(userRequestDto.name());
        userEntity.setEmail(userRequestDto.email());
        userEntity.setPassword(passwordEncoder.encode(userRequestDto.password()));
        userEntity.setPhoneNumber(userRequestDto.phoneNumber());
        userEntity.setRole("USER");
        CartEntity cart = new CartEntity();
        cart.setCartItems(new ArrayList<>());
        userEntity.setCart(cart);
        UserEntity save = userRepo.save(userEntity);

        return new UserResponseDto(save.getId(),save.getName(), save.getEmail(), save.getPhoneNumber());
    }

    public LoginResponseDto login(LoginRequest dto) {

        UserEntity user = userRepo.findByEmail(dto.email())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new UnauthorizedException("Invalid email or password");
        }
        return new LoginResponseDto(user.getId(), jwtService.generateToken(new CustomUserDetails(user)));

    }



    public UserResponseDto getUserById(Long id){
        UserEntity userEntity = userRepo.findById(id).orElseThrow(()-> new RuntimeException("User not found"));
        return new UserResponseDto(userEntity.getId(),userEntity.getName(), userEntity.getEmail(), userEntity.getPhoneNumber());
    }

    @Transactional
    public void logout(String token){

        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Token is missing");
        }

        if (blacklistedTokenRepo.existsByToken(token)) {
            return; // already logged out, idempotent
        }

        Date date = jwtService.extractExpiration(token);

        if (date.before(new Date())) {
            return; // token already expired, no need to blacklist
        }

        BlacklistedToken blacklistedToken = new BlacklistedToken();
        blacklistedToken.setToken(token);
        blacklistedToken.setExpiresAt(date);
        blacklistedTokenRepo.save(blacklistedToken);
    }

    @Transactional
    public UserResponseDto updateUser(Long userId, UserRequestDto userRequestDto){
        UserEntity userEntity = userRepo.findById(userId).orElseThrow(()-> new UserNotFound("User not found"));
        userEntity.setName(userRequestDto.name());
        userEntity.setEmail(userRequestDto.email());
        userEntity.setPhoneNumber(userRequestDto.phoneNumber());
        if(userRequestDto.password() != null && !userRequestDto.password().isEmpty()){
            userEntity.setPassword(passwordEncoder.encode(userRequestDto.password()));
        }
        UserEntity save = userRepo.save(userEntity);
        return new UserResponseDto(save.getId(),save.getName(), save.getEmail(), save.getPhoneNumber());
    }
}
