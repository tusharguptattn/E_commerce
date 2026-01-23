package com.ecommerce.ecommerce.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@Setter
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "cartRegion")
@Table(name = "cart_items",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"CART_ID", "PRODUCT_VARIATION_ID"})
    })
public class CartItemEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "cart_Item_seq")
  @SequenceGenerator(name = "cart_Item_seq",sequenceName = "cart_Item_seq_gen",initialValue = 1000,allocationSize = 10)
  @Column(name = "ID")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "CART_ID", nullable = false)
  private CartEntity cart;

  @ManyToOne
  @JoinColumn(name = "PRODUCT_VARIATION_ID", nullable = false)
  private ProductVariation productVariation;

  @Column(nullable = false)
  private boolean inWishlist;

  @Column(nullable = false)
  private int quantity;

  @CreationTimestamp
  private LocalDateTime createdAt;

}
