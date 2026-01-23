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
@Table(name = "CUSTOMER")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "userRegion")
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "customer_seq")
    @SequenceGenerator(name = "customer_seq",sequenceName = "customer_seq_gen",initialValue = 100,allocationSize = 10)
    @Column(name = "ID")
    private Long customerId;

    @OneToOne
    @JoinColumn(name = "USER_ID")
    private UserEntity user;

    @Column(name = "CONTACT", nullable = false, unique = true)
    private String contactNumber;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

  @Embedded
  private CreateAndUpdatedBy createAndUpdatedBy;

}
