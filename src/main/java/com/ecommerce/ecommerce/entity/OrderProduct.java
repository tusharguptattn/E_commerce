package com.ecommerce.ecommerce.entity;

import com.ecommerce.ecommerce.entity.embeddable.CreateAndUpdatedBy;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ORDER_PRODUCT")
@NoArgsConstructor
@Getter
@Setter
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "orderRegion")
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "order_product_seq")
    @SequenceGenerator(name = "order_product_seq",sequenceName = "order_product_seq_gen",initialValue = 100,allocationSize = 10)
    @Column(name = "ID")
    private Long itemId;

    @Column(name = "QUANTITY", nullable = false)
    private int quantity;

    @Column(name = "PRICE", nullable = false)
    private BigDecimal price;


    @ManyToOne
    @JoinColumn(name = "ORDER_ID", nullable = false)
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_VARIATION_ID", nullable = false)
    private ProductVariation product;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

  @Embedded
  private CreateAndUpdatedBy createAndUpdatedBy;




}
