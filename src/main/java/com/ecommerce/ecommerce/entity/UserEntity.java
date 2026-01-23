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
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Table(name = "users")
@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "userRegion")
@NoArgsConstructor
@Getter
@Setter

public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq_gen")
    @SequenceGenerator(name = "user_seq_gen", sequenceName = "user_seq_gen", initialValue = 100, allocationSize = 10)
    @Column(name = "ID")
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(name = "FIRST_NAME", length = 50, nullable = false)
    private String firstName;

    @Column(name = "MIDDLE_NAME", length = 50)
    private String middleName;

    @Column(name = "LAST_NAME", length = 50, nullable = false)
    private String lastName;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "IS_DELETED", nullable = false)
    private boolean isDeleted = false;

    @Column(name = "IS_ACTIVE", nullable = false)
    private boolean isActive = false;

    @Column(name = "IS_EXPIRED", nullable = false)
    private boolean isExpired = false;

    @Column(name = "IS_LOCKED", nullable = false)
    private boolean isLocked = false;

    @Column(name = "INVALID_ATTEMPT_COUNT", nullable = false)
    private int invalidAttemptCount = 0;

    @Column(name = "PASSWORD_UPDATED_DATE")
    private LocalDateTime passwordUpdatedDate;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},fetch = FetchType.EAGER)
    @JoinTable(name = "USER_ROLE", joinColumns = {
            @JoinColumn(name = "USER_ID"),
            }, inverseJoinColumns = {
            @JoinColumn(name = "ROLE_ID")
    }
    )
    private Set<RoleEntity> roles = new HashSet<>();


    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

  @Embedded
  private CreateAndUpdatedBy createAndUpdatedBy;


    @Version
    private Long version;


    @Transient
    public String getFullName() {
        return Stream.of(firstName, middleName, lastName)
                .filter(Objects::nonNull)
                .filter(s -> !s.isBlank())
                .collect(Collectors.joining(" "));
    }




}
