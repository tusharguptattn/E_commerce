package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<RoleEntity,Long> {
    RoleEntity findByAuthority(String authority);
    boolean existsByAuthority(String authority);
}
