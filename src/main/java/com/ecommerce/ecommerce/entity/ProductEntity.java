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

@Entity
@Table(name = "PRODUCT", uniqueConstraints = @UniqueConstraint(
        columnNames = {"NAME", "BRAND", "CATEGORY_ID", "SELLER_USER_ID"}
))
@NoArgsConstructor
@Getter
@Setter

@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "productRegion")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "product_seq")
    @SequenceGenerator(name = "product_seq",sequenceName = "product_seq_gen",initialValue = 100,allocationSize = 10)
    @Column(name="ID")
    private Long productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SELLER_USER_ID")
    private SellerEntity seller;


    @Column(name = "NAME",nullable = false)
    private String productName;

    @Column(name = "DESCRIPTION",length = 2000)
    private String description;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID",nullable = false)
    private CategoryEntity category;


    @Column(name = "IS_CANCELLABLE",nullable = false)
    private boolean isCancellable;

    @Column(name = "IS_RETURNABLE",nullable = false)
    private boolean isReturnable;

    @Column(name = "BRAND",nullable = false)
    private String brand;

    @Column(name = "IS_ACTIVE",nullable = false)
    private boolean isActive=false;

    @Column(name = "IS_DELETED",nullable = false)
    private boolean isDeleted=false;





    @Version
    private Long version;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

  @Embedded
  private CreateAndUpdatedBy createAndUpdatedBy;


}
