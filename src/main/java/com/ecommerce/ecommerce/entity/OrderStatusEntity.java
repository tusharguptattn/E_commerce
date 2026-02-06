package com.ecommerce.ecommerce.entity;

import com.ecommerce.ecommerce.entity.embeddable.CreateAndUpdatedBy;
import com.ecommerce.ecommerce.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "ORDER_STATUS")
@NoArgsConstructor
@Getter
@Setter
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "orderRegion")
public class OrderStatusEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "order_status_seq")
    @SequenceGenerator(name = "order_status_seq",sequenceName = "order_status_seq_gen",initialValue = 100,allocationSize = 10)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ORDER_PRODUCT_ID", nullable = false)
    private OrderProduct orderProduct;


    @Enumerated(EnumType.STRING)
    @Column(name = "FROM_STATUS")
    private OrderStatus fromStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "TO_STATUS", nullable = false)
    private OrderStatus toStatus;

    @Column(name = "TRANSITION_NOTES_COMMENTS")
    private String transitionNotesComments;

    @Column(name = "TRANSITION_DATE", nullable = false)
    @CreationTimestamp
    private LocalDateTime timestamp;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

  @Embedded
  private CreateAndUpdatedBy createAndUpdatedBy = new CreateAndUpdatedBy();




}
