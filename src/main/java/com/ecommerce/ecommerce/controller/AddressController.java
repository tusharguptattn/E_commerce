package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dto.AddressDto;
import com.ecommerce.ecommerce.securityConfig.JwtService;
import com.ecommerce.ecommerce.securityConfig.SecurityUtil;
import com.ecommerce.ecommerce.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {
    private AddressService addressService;
    private SecurityUtil securityUtil;
    public AddressController(AddressService addressService, JwtService jwtService,SecurityUtil securityUtil){
        this.addressService=addressService;
        this.securityUtil=securityUtil;
    }

    @PostMapping
    public ResponseEntity<AddressDto> addAddress(@RequestBody @Valid AddressDto addressDto)  {
        AddressDto addressResponseDto = addressService.addAddress(addressDto,securityUtil.getCurrentUserId());
        return ResponseEntity.ok(addressResponseDto);
    }
    @GetMapping("/user")
    public ResponseEntity<List<AddressDto>> getAllAddress(){
        return ResponseEntity.ok(addressService.getAllAddress(securityUtil.getCurrentUserId()));
    }
    @PutMapping("/{addressId}")
    public ResponseEntity<AddressDto> updateAddress(@RequestBody  @Valid AddressDto addressDto,@PathVariable Long addressId)  {
        return ResponseEntity.ok(addressService.updateAddress(addressDto, addressId,securityUtil.getCurrentUserId()));

    }
    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long addressId){
        boolean b = addressService.deleteAddress(addressId, securityUtil.getCurrentUserId());
        if(b){
            return ResponseEntity.noContent().build();
        }else {
            return ResponseEntity.notFound().build();
        }
    }




}
