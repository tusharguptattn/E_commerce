package com.ecommerce.ecommerce.entity;

import com.ecommerce.ecommerce.entity.embeddable.CreateAndUpdatedBy;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "carts")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "cartRegion")
@NoArgsConstructor
@Getter
@Setter
public class CartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "cart_seq")
    @SequenceGenerator(name = "cart_seq",sequenceName = "cart_seq_gen",initialValue = 1000,allocationSize = 10)
    @Column(name = "ID")
    private Long id;


    @OneToOne
    @JoinColumn(name = "CUSTOMER_USER_ID",nullable = false,unique = true,referencedColumnName = "USER_ID")
    private CustomerEntity customer;


  @OneToMany(
      mappedBy = "cart",
      cascade = CascadeType.ALL,
      orphanRemoval = true
  )
  private List<CartItemEntity> items;



    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

  @Embedded
  private CreateAndUpdatedBy createAndUpdatedBy;


    @Version
    private Long version;


}

