package com.ecommerce.ecommerce.entity;

import com.ecommerce.ecommerce.entity.embeddable.CreateAndUpdatedBy;
import com.ecommerce.ecommerce.entity.embeddable.OrderAddress;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "orders")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "orderRegion")
@NoArgsConstructor
@Getter
@Setter
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "order_seq")
    @SequenceGenerator(name = "order_seq",sequenceName = "order_seq_gen",initialValue = 100,allocationSize = 10)
    @Column(name = "ID")
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "CUSTOMER_USER_ID",nullable = false)
    private CustomerEntity customer;

    @Column(name="AMOUNT_PAID",nullable = false)
    private BigDecimal amountPaid;


    @CreationTimestamp
    @Column(updatable = false,nullable = false,name = "DATE_CREATED")
    private Date createdAt;

    @Column(name = "PAYMENT_METHOD",nullable = false)
    private String paymentMethod;

    @Embedded
    private OrderAddress address;


    @Version
    private Long version;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

  @Embedded
  private CreateAndUpdatedBy createAndUpdatedBy;




}

