package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.entity.UserActivationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserActivationTokenRepo extends JpaRepository<UserActivationToken,Long> {
    Optional<UserActivationToken> findByToken(String token);
    Optional<UserActivationToken> findByUser_Id(Long userId);

}
