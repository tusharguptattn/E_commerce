package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.entity.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlacklistedTokenRepo extends JpaRepository<BlacklistedToken,Long> {
    boolean existsByToken(String token);
}
