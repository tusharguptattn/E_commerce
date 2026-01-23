package com.ecommerce.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "")
    @SequenceGenerator(name="password_reset_token_gen", sequenceName = "password_reset_token_seq_gen", initialValue = 1, allocationSize = 5)

    private Long id;

    @Column(unique = true, nullable = false)
    private String token;

    @OneToOne
    private UserEntity user;

    private LocalDateTime expiryTime;
}

