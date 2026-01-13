package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dto.LoginRequest;
import com.ecommerce.ecommerce.dto.LoginResponseDto;
import com.ecommerce.ecommerce.dto.UserRequestDto;
import com.ecommerce.ecommerce.dto.UserResponseDto;
import com.ecommerce.ecommerce.securityConfig.SecurityUtil;
import com.ecommerce.ecommerce.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final SecurityUtil securityUtil;
    public UserController(UserService userService, SecurityUtil securityUtil){
        this.userService = userService;
        this.securityUtil = securityUtil;
    }



    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(@RequestBody @Valid UserRequestDto userRequestDto) {
        // Registration logic goes here
        return ResponseEntity.ok(userService.registerUser(userRequestDto));
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequest dto) {
        return ResponseEntity.ok(userService.login(dto));
    }
    @PostMapping("/logout")
    public ResponseEntity<Void> logoutUser(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            userService.logout(token);

        }

        return ResponseEntity.noContent().build();
    }



    @GetMapping
    public ResponseEntity<UserResponseDto> getUserById(){
        return ResponseEntity.ok(userService.getUserById(securityUtil.getCurrentUserId()));
    }
    @PutMapping
    public ResponseEntity<UserResponseDto> updateUser(@RequestBody @Valid UserRequestDto userRequestDto){
        return ResponseEntity.ok(userService.updateUser(securityUtil.getCurrentUserId(),userRequestDto));
    }

}
