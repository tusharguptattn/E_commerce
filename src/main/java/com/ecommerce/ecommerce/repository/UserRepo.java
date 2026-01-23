package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.dto.UserResponseDto;
import com.ecommerce.ecommerce.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserEntity,Long> {
    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT u.id,u.firstName,u.middleName,u.lastName,u.email,u.isActive FROM UserEntity u JOIN u.roles r WHERE r.authority = ?1")
    Page<UserResponseDto> findByRoles_Authority(String authority, Pageable pageable);
}

