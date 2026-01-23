package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.AddressDto;
import com.ecommerce.ecommerce.dto.CustomerUpdateRequestDto;
import com.ecommerce.ecommerce.dto.CustomerViewMyProfileDto;
import com.ecommerce.ecommerce.entity.AddressEntity;
import com.ecommerce.ecommerce.entity.CustomerEntity;
import com.ecommerce.ecommerce.entity.UserEntity;
import com.ecommerce.ecommerce.exceptionHanding.BadRequest;
import com.ecommerce.ecommerce.repository.AddressRepo;
import com.ecommerce.ecommerce.repository.CustomerRepo;
import com.ecommerce.ecommerce.repository.UserRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerService {

  CustomerRepo customerRepo;
  UserRepo userRepo;
  AddressRepo addressRepo;
  PasswordEncoder passwordEncoder;
  EmailService emailService;


  public CustomerViewMyProfileDto getProfile(Long userId) {
    UserEntity userEntity = userRepo.findById(userId)
        .orElseThrow(() -> new BadRequest("User not found"));
    String contactNumberByUserId = customerRepo.findContactNumberByUserId(userId);

    return new CustomerViewMyProfileDto(userEntity.getId(),
        userEntity.getFirstName(),
        userEntity.getLastName(),
        contactNumberByUserId,
        null,
        userEntity.isActive());

  }


  public List<AddressDto> viewAddress(Long userId) {
    List<AddressEntity> allAddressesByUserId = addressRepo.findAllAddressesByUserId(userId);

    return allAddressesByUserId.stream().map(addressEntity -> new AddressDto(
        addressEntity.getStreet(),
        addressEntity.getCity(),
        addressEntity.getState(),
        addressEntity.getZipcode(),
        addressEntity.getCountry(),
        addressEntity.getLabel()
    )).toList();

  }

  @Transactional
  public void updateProfile(CustomerUpdateRequestDto customerUpdateRequestDto, Long userId) {
    UserEntity userEntity = userRepo.findById(userId)
        .orElseThrow(() -> new BadRequest("User not found"));

    CustomerEntity customerEntity = customerRepo.findByUserId(userId)
        .orElseThrow(() -> new BadRequest("Customer not found"));

    if (customerUpdateRequestDto.firstName() != null && !customerUpdateRequestDto.firstName()
        .isBlank()) {
      userEntity.setFirstName(customerUpdateRequestDto.firstName());
    }
    if (customerUpdateRequestDto.lastName() != null && !customerUpdateRequestDto.lastName()
        .isBlank()) {
      userEntity.setLastName(customerUpdateRequestDto.lastName());
    }

    if (customerUpdateRequestDto.middleName() != null && !customerUpdateRequestDto.middleName()
        .isBlank()) {
      userEntity.setMiddleName((customerUpdateRequestDto.middleName()));
    }

    if (customerUpdateRequestDto.contactNumber() != null
        && !customerUpdateRequestDto.contactNumber().isBlank()) {
      boolean exists = customerRepo.existsByContactNumberOrUser_Email(
          customerUpdateRequestDto.contactNumber(), null);
      if (exists && !customerEntity.getContactNumber()
          .equals(customerUpdateRequestDto.contactNumber())) {
        throw new BadRequest("Contact number already in use");
      } else {
        customerEntity.setContactNumber(customerUpdateRequestDto.contactNumber());
        customerRepo.save(customerEntity);
      }
    }

    userRepo.save(userEntity);

  }

  @Transactional
  public void updatePassword(String newPassword, String confirmPassword, Long userId) {
    UserEntity userEntity = userRepo.findById(userId)
        .orElseThrow(() -> new BadRequest("User not found"));
    if (!newPassword.equals(confirmPassword)) {
      throw new BadRequest("New password and confirm password do not match");
    }
    userEntity.setPassword(passwordEncoder.encode(newPassword));
    userRepo.save(userEntity);
    emailService.sendPasswordChangeConfirmationEmail(userEntity.getEmail(),
        userEntity.getFirstName());

  }

  @Transactional
  public void addNewAddress(AddressDto addressDto, Long userId) {
    UserEntity userEntity = userRepo.findById(userId)
        .orElseThrow(() -> new BadRequest("User not found"));

    AddressEntity addressEntity = new AddressEntity();
    addressEntity.setStreet(addressDto.street());
    addressEntity.setCity(addressDto.city());
    addressEntity.setState(addressDto.state());
    addressEntity.setZipcode(addressDto.zipcode());
    addressEntity.setCountry(addressDto.country());
    addressEntity.setLabel(addressDto.label());
    addressEntity.setUser(userEntity);

    addressRepo.save(addressEntity);

  }

  @Transactional
  public void deleteAddress(Long addressId, Long userId) {
    AddressEntity addressEntity = addressRepo.findById(addressId)
        .orElseThrow(() -> new BadRequest("Address not found"));
    if (!addressEntity.getUser().getId().equals(userId)) {
      throw new BadRequest("You are not authorized to delete this address");
    }
    addressRepo.delete(addressEntity);
  }

  @Transactional
  public void updateAddress(Long addressId, Long userId, AddressDto addressDto) {
    AddressEntity addressEntity = addressRepo.findById(addressId)
        .orElseThrow(() -> new BadRequest("Address not found"));
    if (!addressEntity.getUser().getId().equals(userId)) {
      throw new BadRequest("You are not authorized to delete this address");
    }
    addressEntity.setStreet(addressDto.street());
    addressEntity.setCity(addressDto.city());
    addressEntity.setState(addressDto.state());
    addressEntity.setZipcode(addressDto.zipcode());
    addressEntity.setCountry(addressDto.country());
    addressEntity.setLabel(addressDto.label());
    addressRepo.save(addressEntity);
  }


}
