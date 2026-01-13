package com.ecommerce.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "blacklisted_tokens")
@Setter
public class BlacklistedToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false, unique = true)
    private String token;

    private Date expiresAt;
}
