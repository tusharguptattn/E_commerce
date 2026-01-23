package com.ecommerce.ecommerce.entity;

import com.ecommerce.ecommerce.entity.embeddable.CreateAndUpdatedBy;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Getter
@Setter
@Table(name = "PRODUCT_VARIATION")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "productRegion")
public class ProductVariation {
    @Column(name = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "product_variation_seq")
    @SequenceGenerator(name = "product_variation_seq",sequenceName = "product_variation_seq_gen",initialValue = 100,allocationSize = 10)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private ProductEntity product;

    @Column(name = "QUANTITY_AVAILABLE", nullable = false)
    private int quantityAvailable;

    @Column(name = "PRICE", nullable = false)
    private BigDecimal price;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "METADATA", columnDefinition = "jsonb")
    private Map<String,Object> metadata;


    @Column(name = "PRIMARY_IMAGE_NAME")
    private String primaryImageName;

    @Column(name = "IS_ACTIVE", nullable = false)
    private boolean isActive;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

  @Embedded
  private CreateAndUpdatedBy createAndUpdatedBy;


}
