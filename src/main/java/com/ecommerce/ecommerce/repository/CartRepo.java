package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.entity.CartEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepo extends JpaRepository<CartEntity,Long> {

    CartEntity findByCustomer_User_Id(Long userId);

}
