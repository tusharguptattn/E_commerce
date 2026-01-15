package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.SellerApplyRequestDto;
import com.ecommerce.ecommerce.entity.SellerEntity;
import com.ecommerce.ecommerce.entity.UserEntity;
import com.ecommerce.ecommerce.exceptionHanding.ResourceNotFoundException;
import com.ecommerce.ecommerce.repository.SellerRepo;
import com.ecommerce.ecommerce.repository.UserRepo;
import org.springframework.stereotype.Service;

@Service
public class SellerService {

    private final SellerRepo sellerRepo;
    private final UserRepo userRepo;

    public SellerService(SellerRepo sellerRepo,UserRepo userRepo) {
        this.sellerRepo = sellerRepo;
        this.userRepo=userRepo;
    }


    public void applyForSeller(SellerApplyRequestDto sellerApplyRequestDto,Long userId){
        // Implementation for seller application logic goes here
        UserEntity userEntity = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if(sellerRepo.findByUser_Id(userId).isPresent()){
            throw new ResourceNotFoundException("Seller application already exists for this user");
        }

        SellerEntity sellerEntity = new SellerEntity();
        sellerEntity.setBusinessName(sellerApplyRequestDto.businessName());
        sellerEntity.setGstNumber(sellerApplyRequestDto.gstNumber());
        sellerEntity.setBankAccountNumber(sellerApplyRequestDto.bankAccountNumber());
        sellerEntity.setUser(userEntity);
        sellerEntity.setIfscCode(sellerApplyRequestDto.ifscCode());
        sellerRepo.save(sellerEntity);

    }
}
