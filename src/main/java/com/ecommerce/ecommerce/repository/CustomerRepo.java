package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerRepo extends JpaRepository<CustomerEntity,Long> {
    boolean existsByContactNumberOrUser_Email(String contactNumber, String email);
    @Query("""
    select c.contactNumber
    from CustomerEntity c
    where c.user.id = :userId
""")
    String findContactNumberByUserId(@Param("userId") Long userId);

    Optional<CustomerEntity> findByUserId(Long userId);

}
