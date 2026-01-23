package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepo extends JpaRepository<AddressEntity,Long> {
    List<AddressEntity> findAllAddressesByUserId(Long userId);
    Optional<AddressEntity> findByUser_Id(Long userId);
}
