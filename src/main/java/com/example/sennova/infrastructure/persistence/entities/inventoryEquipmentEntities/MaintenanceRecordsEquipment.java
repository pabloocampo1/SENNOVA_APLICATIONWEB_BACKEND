package com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;


@Table(name = "maintenance_record_equipment")
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
public class MaintenanceRecordsEquipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maintenance_record_equipment_id")
    private Long userId;

    @Column(nullable = false)
    private String performed_by;

    private String maintenance_type;

    @Column(length = 400)
    private String notes;

    @CreatedDate
    private LocalDate createAt;

    @LastModifiedDate
    private LocalDate updateAt;

    @ManyToOne
    @JoinColumn(name = "equipment_id", referencedColumnName = "equipment_id")
    private EquipmentEntity equipment;

}
