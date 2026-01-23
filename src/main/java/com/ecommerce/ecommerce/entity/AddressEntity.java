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
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "addressRegion")
@Table(name = "addresses")
@NoArgsConstructor
@Getter
@Setter
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "address_seq_gen")
    @SequenceGenerator(name = "address_seq_gen",sequenceName = "address_seq_gen",initialValue = 100,allocationSize = 10)
    @Column(name = "ID")
    private Long addressId;

    @Column(name = "ADDRESS_LINE", nullable = false)
    private String street;

    @Column(name = "CITY", nullable = false)
    private String city;
    @Column(name = "STATE", nullable = false)
    private String state;

    @Column(name = "ZIP_CODE", nullable = false)
    private String zipcode;

    @Column(name = "COUNTRY", nullable = false)
    private String country;

    @Column(name = "LABEL")
    private String label;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserEntity user;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Embedded
    private CreateAndUpdatedBy createAndUpdatedBy;

}
