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
@Setter
@Getter
@NoArgsConstructor
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "categoryRegion")
@Table(name = "CATEGORY_METADATA_FIELD")
public class CategoryMetaDataField {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "category_metadata_seq")
    @SequenceGenerator(name = "category_metadata_seq",sequenceName = "category_metadata_seq_gen",initialValue = 100,allocationSize = 10)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME",nullable = false,unique = true)
    private String name;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

  @Embedded
  private CreateAndUpdatedBy createAndUpdatedBy;


}
