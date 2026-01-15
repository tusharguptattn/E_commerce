package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dto.SellerApplyRequestDto;
import com.ecommerce.ecommerce.securityConfig.SecurityUtil;
import com.ecommerce.ecommerce.service.SellerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sellers")
public class SellerController {

    private final SellerService sellerService;
    private final SecurityUtil securityUtil;

    public SellerController(SellerService sellerService,SecurityUtil securityUtil) {
        this.sellerService = sellerService;
        this.securityUtil = securityUtil;

    }

    @PostMapping("/apply")
    public ResponseEntity<String> applyForSeller(@RequestBody @Valid SellerApplyRequestDto sellerApplyRequestDto) {
        sellerService.applyForSeller(sellerApplyRequestDto,securityUtil.getCurrentUserId());
        return ResponseEntity.ok("Seller application submitted successfully");
    }
}
