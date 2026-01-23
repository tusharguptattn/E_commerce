package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepo extends JpaRepository<PasswordResetToken,Long> {
    boolean existsByUser_Id(Long userId);
    PasswordResetToken findByUser_Id(Long userId);
    Optional<PasswordResetToken> findByToken(String token);
}
