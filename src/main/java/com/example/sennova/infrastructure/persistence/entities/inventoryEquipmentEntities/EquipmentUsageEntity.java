package com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities;


import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name = "equipment_usage")
@Data
@EntityListeners(AuditingEntityListener.class)
public class EquipmentUsageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "equipment_usage_id")
    private Long equipmentUsageId;

    @Column(nullable = false)
    private String usage_name;

    @CreatedDate
    private LocalDate createAt;

    @LastModifiedDate
    private LocalDate updateAt;

    @OneToMany(mappedBy = "usage")
    private List<EquipmentEntity> equipmentEntityList;

}
