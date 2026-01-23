package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.entity.CompanyAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyAddressRepo extends JpaRepository<CompanyAddressEntity,Long> {

  CompanyAddressEntity findBySellerEntity_SellerId(Long sellerId);


}
