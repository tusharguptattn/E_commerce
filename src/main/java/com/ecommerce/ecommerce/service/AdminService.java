package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.CustomerResponseDto;
import com.ecommerce.ecommerce.dto.SellerResponseDto;
import com.ecommerce.ecommerce.dto.UserResponseDto;
import com.ecommerce.ecommerce.entity.SellerEntity;
import com.ecommerce.ecommerce.entity.UserEntity;
import com.ecommerce.ecommerce.exceptionHanding.BadRequest;
import com.ecommerce.ecommerce.repository.CustomerRepo;
import com.ecommerce.ecommerce.repository.SellerRepo;
import com.ecommerce.ecommerce.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepo userRepo;
    private final SellerRepo sellerRepo;
    private final EmailService emailService;


    public Page<CustomerResponseDto> getAllRegisteredCustomers(Pageable pageable){
        Page<UserResponseDto> customer = userRepo.findByRoles_Authority("ROLE_CUSTOMER", pageable);
        return customer.map(userEntity -> new CustomerResponseDto(
                userEntity.id(),
                userEntity.firstName()+" "+userEntity.lastName(),
                userEntity.email(),
                userEntity.isActive())
        );

    }



    public Page<SellerResponseDto> getAllRegisteredSeller(Pageable pageable){
        Page<SellerEntity> seller = sellerRepo.findAll(pageable);

        return seller.map(sellerEntity -> new SellerResponseDto(
                sellerEntity.getUser().getId(),
               sellerEntity.getUser().getFullName(),
                sellerEntity.getUser().getEmail(),
                sellerEntity.getUser().isActive(),
                sellerEntity.getBusinessName(),
                sellerEntity.getGstNumber(),
                sellerEntity.getCompanyContactNumber()
        ));

    }


    public void activateCustomerAccount(Long customerId) {
        UserEntity user = userRepo.findById(customerId)
                .orElseThrow(() -> new BadRequest("Customer not found"));
        if(user.isActive()){
            return;
        }
        user.setActive(true);
        userRepo.save(user);
        emailService.sendAccountActivationEmail(user.getEmail(), user.getFirstName());
    }

    public void deActivateCustomerAccount(Long customerId) {
        UserEntity user = userRepo.findById(customerId)
                .orElseThrow(() -> new BadRequest("Customer not found"));
        if(!user.isActive()){
            return;
        }
        user.setActive(false);
        userRepo.save(user);
        emailService.sendAccountDeactivationEmail(user.getEmail(), user.getFirstName());
    }





}
