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
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "ROLE")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "userRegion")
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "role_seq")
    @SequenceGenerator(name = "role_seq",sequenceName = "role_seq_gen",initialValue = 100,allocationSize = 10)
    @Column(name = "ID")
    private Long id;

    @Column(name = "AUTHORITY",nullable = false,unique = true)
    private String authority;

    @ManyToMany(mappedBy = "roles")
    private Set<UserEntity> user = new HashSet<>();

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

  @Embedded
  private CreateAndUpdatedBy createAndUpdatedBy;

}
