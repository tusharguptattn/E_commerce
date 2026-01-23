package com.ecommerce.ecommerce.entity;

import com.ecommerce.ecommerce.entity.embeddable.CreateAndUpdatedBy;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "company_address")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "addressRegion")
public class CompanyAddressEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "address_seq_gen")
  @SequenceGenerator(name = "address_seq_gen",sequenceName = "address_seq_gen",initialValue = 100,allocationSize = 10)
  @Column(name = "ID")
  private Long id;

  @Column(name = "ADDRESS_LINE", nullable = false)
  private String street;

  @Column(name = "CITY", nullable = false)
  private String city;
  @Column(name = "STATE", nullable = false)
  private String state;

  @Column(name = "ZIP_CODE", nullable = false)
  private String zipcode;

  @Column(name = "COUNTRY", nullable = false)
  private String country;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "SELLER_ID", nullable = false)
  private SellerEntity sellerEntity;

  @CreationTimestamp
  private LocalDateTime createdAt;
  @UpdateTimestamp
  private LocalDateTime updatedAt;

  @Embedded
  private CreateAndUpdatedBy createAndUpdatedBy;




}
