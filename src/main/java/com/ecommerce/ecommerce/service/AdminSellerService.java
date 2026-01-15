package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.entity.SellerEntity;
import com.ecommerce.ecommerce.entity.UserEntity;
import com.ecommerce.ecommerce.enums.Roles;
import com.ecommerce.ecommerce.enums.SellerStatus;
import com.ecommerce.ecommerce.exceptionHanding.ResourceNotFoundException;
import com.ecommerce.ecommerce.repository.SellerRepo;
import com.ecommerce.ecommerce.repository.UserRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminSellerService {
    private final UserRepo userRepo;
    private final SellerRepo sellerRepo;

    public AdminSellerService(UserRepo userRepo, SellerRepo sellerRepo) {
        this.userRepo = userRepo;
        this.sellerRepo = sellerRepo;
    }

    @Transactional
    public void approveSellerApplication(Long sellerApplicationId) {
        SellerEntity sellerApplication = sellerRepo.findById(sellerApplicationId).orElseThrow(() -> new ResourceNotFoundException("Seller Application not found"));
        if (sellerApplication.getStatus() != SellerStatus.PENDING) {
            throw new IllegalStateException("Seller already processed");
        }
        UserEntity user = sellerApplication.getUser();
        user.setRole(Roles.SELLER);
        userRepo.save(user);
        sellerApplication.setStatus(SellerStatus.APPROVED);
        sellerRepo.save(sellerApplication);
    }


}
