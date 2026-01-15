package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.service.AdminSellerService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/sellers")
@PreAuthorize("hasRole('ADMIN')")
public class AdminSellerController {

    private AdminSellerService adminSellerService;

    public AdminSellerController(AdminSellerService adminSellerService){
        this.adminSellerService = adminSellerService;
    }

    @PutMapping("/approve/{sellerApplicationId}")
    public void approveSellerApplication(@PathVariable("sellerApplicationId") Long sellerApplicationId) {
            adminSellerService.approveSellerApplication(sellerApplicationId);
    }


}
