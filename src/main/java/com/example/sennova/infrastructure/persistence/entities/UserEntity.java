package com.example.sennova.infrastructure.persistence.entities;

import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.TestRequestEntity;
import com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities.EquipmentEntity;
import com.example.sennova.infrastructure.persistence.entities.inventoryReagentsEntities.ReagentsEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.List;

@Table(name = "user")
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private Long dni;

    private boolean available;

    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private Long phoneNumber;

    @Column(nullable = false, unique = true)
    private String email;

    private String position;

    private String imageProfile;


    @CreatedDate
    private LocalDate createAt;

    @LastModifiedDate
    private LocalDate updateAt;

    // relationships
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    private RoleEntity role;

    @ManyToMany(mappedBy = "members")
    private List<TestRequestEntity> testRequestEntities;

    @OneToMany(mappedBy = "responsible")
    private List<EquipmentEntity> equipmentEntityList;

    @OneToMany(mappedBy = "user")
    private List<ReagentsEntity> reagentsEntityList;
}
