package com.ecommerce.ecommerce.entity;

import com.ecommerce.ecommerce.entity.embeddable.CreateAndUpdatedBy;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "CATEGORY_METADATA_FIELD_VALUES")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "categoryRegion")
public class CategoryMetaDataFieldValues {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "category_metadata_value_seq")
    @SequenceGenerator(name = "category_metadata_value_seq",sequenceName = "category_metadata_value_seq_gen",initialValue = 100,allocationSize = 10)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "CATEGORY_METADATA_FIELD_ID")
    private CategoryMetaDataField categoryMetaDataField;

    @ManyToOne(optional = false)
    @JoinColumn(name = "CATEGORY_ID")
    private CategoryEntity categoryEntity;

    @Column(name = "VALUES", nullable = false)
    private String values;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

  @Embedded
  private CreateAndUpdatedBy createAndUpdatedBy;



}
