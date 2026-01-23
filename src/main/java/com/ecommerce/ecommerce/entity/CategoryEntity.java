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
@Table(name = "category_table")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "categoryRegion")
@NoArgsConstructor
@Getter
@Setter
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "category_seq")
    @SequenceGenerator(name = "category_seq",sequenceName = "category_seq_gen",initialValue = 200,allocationSize = 5)
    @Column(name = "ID")
    private Long categoryId;

    @Column(name = "NAME",nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_CATEGORY_ID")
    private CategoryEntity parentCategory;


    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

  @Embedded
  private CreateAndUpdatedBy createAndUpdatedBy;



    @Version
    private Long version;


}
