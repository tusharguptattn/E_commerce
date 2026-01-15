package com.ecommerce.ecommerce.entity;

import com.ecommerce.ecommerce.enums.Roles;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.List;

@Table(name = "user_table",uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "userRegion")
@NoArgsConstructor
@Getter
@Setter

public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq_gen")
    @SequenceGenerator(name="user_seq_gen",sequenceName = "user_seq_gen",initialValue = 100,allocationSize = 10)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String phoneNumber;

    @Version
    private Long version;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Roles role;


    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<AddressEntity> addresses;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="cart_Id")
    private CartEntity cart;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<OrderEntity> orders;

}
