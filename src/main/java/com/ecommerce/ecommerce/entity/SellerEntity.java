package com.ecommerce.ecommerce.entity;

import com.ecommerce.ecommerce.entity.embeddable.CreateAndUpdatedBy;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "sellerRegion")
public class SellerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "seller_seq")
    @SequenceGenerator(name = "seller_seq",sequenceName = "seller_seq_gen",initialValue = 100,allocationSize = 10)
    @Column(name = "ID")
    private Long sellerId;

    @OneToOne
    @JoinColumn(name = "USER_ID")
    private UserEntity user;


    @Column(name = "COMPANY_NAME",nullable = false, unique = true)
    private String businessName;

    @Column(name = "GST", unique = true)
    private String gstNumber;

    @Column(name = "COMPANY_CONTACT", nullable = false, unique = true)
    private String companyContactNumber;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

  @Embedded
  private CreateAndUpdatedBy createAndUpdatedBy;



}
