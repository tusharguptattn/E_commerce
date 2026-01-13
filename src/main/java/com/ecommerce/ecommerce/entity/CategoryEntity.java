package com.ecommerce.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.List;

@Entity
@Table(name = "category_table")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY,region = "categoryRegion")
@NoArgsConstructor
@Getter
@Setter
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_seq_gen")
    @SequenceGenerator(name = "category_seq_gen", sequenceName = "category_seq_gen", initialValue = 25,allocationSize = 5)
    @Column(name = "category_id")
    private Long category;

    @Version
    private Long version;


    @Column(name = "category_name",unique = true)
    private String name;

    @OneToMany(mappedBy = "category",cascade=CascadeType.ALL)
    private List<ProductEntity> products;


}
