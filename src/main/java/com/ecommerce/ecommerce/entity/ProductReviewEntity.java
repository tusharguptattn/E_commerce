package com.ecommerce.ecommerce.entity;

import com.ecommerce.ecommerce.entity.embeddable.CreateAndUpdatedBy;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "PRODUCT_REVIEW")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "productRegion")
public class ProductReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "product_review_seq")
    @SequenceGenerator(name = "product_review_seq",sequenceName = "product_review_seq_gen",initialValue = 100,allocationSize = 10)
    @Column(name = "ID")
    private Long reviewId;
    @ManyToOne
    @JoinColumn(name = "CUSTOMER_USER_ID",referencedColumnName = "USER_ID")
    private CustomerEntity customer;

    @Column(name = "REVIEW")
    private String reviewText;

    @Column(name = "RATING",nullable = false)
    private BigDecimal rating;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID",nullable = false)
    private ProductEntity product;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

  @Embedded
  private CreateAndUpdatedBy createAndUpdatedBy;




}
