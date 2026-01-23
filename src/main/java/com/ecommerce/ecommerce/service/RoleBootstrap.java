//package com.ecommerce.ecommerce.service;
//
//import com.ecommerce.ecommerce.entity.RoleEntity;
//import com.ecommerce.ecommerce.repository.RoleRepo;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class RoleBootstrap {
//
//    private final RoleRepo roleRepo;
//
//    @PostConstruct
//    public void init() {
//        createIfNotExists("ROLE_ADMIN");
//        createIfNotExists("ROLE_SELLER");
//        createIfNotExists("ROLE_CUSTOMER");
//    }
//
//    private void createIfNotExists(String role) {
//        if (!roleRepo.existsByAuthority(role)) {
//            RoleEntity r = new RoleEntity();
//            r.setAuthority(role);
//
//            roleRepo.save(r);
//        }
//    }
//}
