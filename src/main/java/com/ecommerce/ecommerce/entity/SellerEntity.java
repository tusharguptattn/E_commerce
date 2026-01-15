package com.ecommerce.ecommerce.entity;

import com.ecommerce.ecommerce.enums.SellerStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SellerEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long sellerId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    private SellerStatus status=SellerStatus.PENDING; // PENDING, APPROVED

    private String businessName;
    private String gstNumber;
    private String bankAccountNumber;
    private String ifscCode;


}
