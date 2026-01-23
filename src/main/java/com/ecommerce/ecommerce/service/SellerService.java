package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.CompanyAddressRequestDto;
import com.ecommerce.ecommerce.dto.ResetPasswordRequestDto;
import com.ecommerce.ecommerce.dto.SellerProfileUpdateRequestDto;
import com.ecommerce.ecommerce.dto.SellerViewMyProfileDto;
import com.ecommerce.ecommerce.entity.AddressEntity;
import com.ecommerce.ecommerce.entity.CompanyAddressEntity;
import com.ecommerce.ecommerce.entity.SellerEntity;
import com.ecommerce.ecommerce.entity.UserEntity;
import com.ecommerce.ecommerce.exceptionHanding.BadRequest;
import com.ecommerce.ecommerce.exceptionHanding.UnauthorizedException;
import com.ecommerce.ecommerce.repository.AddressRepo;
import com.ecommerce.ecommerce.repository.CompanyAddressRepo;
import com.ecommerce.ecommerce.repository.SellerRepo;
import com.ecommerce.ecommerce.repository.UserRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SellerService {

  SellerRepo sellerRepo;
  UserRepo userRepo;
  CompanyAddressRepo companyAddressRepo;
  PasswordEncoder passwordEncoder;


  public SellerViewMyProfileDto getProfile(Long userId) {
    // Implementation for seller application logic goes here
    UserEntity userEntity = userRepo.findById(userId)
        .orElseThrow(() -> new BadRequest("User not found"));
    SellerEntity sellerEntity = sellerRepo.findByUser_Id(userId)
        .orElseThrow(() -> new BadRequest("Seller profile not found"));

    CompanyAddressEntity companyAddressEntity = companyAddressRepo.findBySellerEntity_SellerId(sellerEntity.getSellerId());

    return new SellerViewMyProfileDto(userEntity.getId(), userEntity.getFirstName(),
        userEntity.getLastName(),
        userEntity.isActive(), sellerEntity.getCompanyContactNumber(),
        sellerEntity.getBusinessName(), null, sellerEntity.getGstNumber(),
        companyAddressEntity.getCity(), companyAddressEntity.getState(), companyAddressEntity.getCountry(),
        companyAddressEntity.getZipcode(), companyAddressEntity.getStreet()
    );

  }

  @Transactional
  public void updateProfile(SellerProfileUpdateRequestDto sellerProfileUpdateRequestDto,Long sellerId){
    SellerEntity sellerEntity = sellerRepo.findByUser_Id(sellerId)
        .orElseThrow(() -> new BadRequest("No Seller found with this id"));

    UserEntity user = sellerEntity.getUser();

    user.setFirstName(sellerProfileUpdateRequestDto.firstName());
    user.setLastName(sellerProfileUpdateRequestDto.lastName());
    user.setEmail(sellerProfileUpdateRequestDto.email());
    sellerEntity.setCompanyContactNumber(sellerProfileUpdateRequestDto.companyContact());
    sellerEntity.setBusinessName(sellerProfileUpdateRequestDto.companyName());
    sellerEntity.setGstNumber(sellerProfileUpdateRequestDto.gstNum());

    sellerEntity.setUser(user);

    sellerRepo.save(sellerEntity);


  }

  @Transactional
  public void updatePassword(ResetPasswordRequestDto resetPasswordRequestDto,Long sellerId,Long userId){
    SellerEntity sellerEntity = sellerRepo.findByUser_Id(sellerId)
        .orElseThrow(() -> new BadRequest("Seller Not found"));

    if (!sellerEntity.getUser().getId().equals(userId)) {
      throw new UnauthorizedException("You are not allowed to update this password");
    }


    if(!resetPasswordRequestDto.newPassword().equals(resetPasswordRequestDto.confirmPassword())){
      throw new BadRequest("Password do not match");
    }

    sellerEntity.getUser().setPassword(passwordEncoder.encode(resetPasswordRequestDto.newPassword()));

    sellerRepo.save(sellerEntity);
  }


  @Transactional
  public void updateAdderess(Long addressId, CompanyAddressRequestDto companyAddressRequestDto,Long userId){
    CompanyAddressEntity companyAddressEntity = companyAddressRepo.findById(addressId)
        .orElseThrow(() -> new BadRequest("Address not found with the given id "));

    if (!companyAddressEntity.getSellerEntity().getUser().getId()
        .equals(userId)) {
      throw new UnauthorizedException("You are not allowed to update this address");
    }

    companyAddressEntity.setCity(companyAddressRequestDto.city());
    companyAddressEntity.setCountry(companyAddressRequestDto.country());
    companyAddressEntity.setState(companyAddressRequestDto.state());
    companyAddressEntity.setZipcode(companyAddressRequestDto.zipcode());
    companyAddressEntity.setStreet(companyAddressRequestDto.street());

    companyAddressRepo.save(companyAddressEntity);



  }







}
