package com.ecommerce.ecommerce.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "USER_ACTIVATION_TOKEN")
@Getter
@Setter
@NoArgsConstructor
public class UserActivationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TOKEN", nullable = false, unique = true)
    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false, unique = true)
    private UserEntity user;

    @Column(name = "EXPIRY_DATE", nullable = false)
    private LocalDateTime expiryDate;

    @Column(name = "IS_USED", nullable = false)
    private boolean used = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
