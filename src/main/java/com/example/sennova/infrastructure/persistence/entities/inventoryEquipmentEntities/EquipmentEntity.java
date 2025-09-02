package com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities;


import com.example.sennova.infrastructure.persistence.entities.UserEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "equipment")
@Data
@EntityListeners(AuditingEntityListener.class)
public class EquipmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "equipment_id")
    private Long equipmentId;

    @Column(unique = true, nullable = false)
    private String internal_code;


    @Column(nullable = false)
    private String equipmentName;

    private String brand;

    private String model;

    @Column(unique = true, nullable = false)
    private Long serial_number;

    private LocalDate acquisitionDate;

    private String amperage;

    private String voltage;

    private double equipment_cost;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private UserEntity responsible;

    @ManyToOne()
    @JoinColumn(name = "equipment_location", referencedColumnName = "equipment_location_id", nullable = false)
    private EquipmentLocationEntity location;

    @ManyToOne
    @JoinColumn(name = "usage_id", referencedColumnName = "equipment_usage_id")
    private EquipmentUsageEntity usage;

    @OneToMany(mappedBy = "equipment")
    private List<EquipmentLoanEntity> loanEntities;

    @OneToMany(mappedBy = "equipment")
    private List<MaintenanceRecordsEquipment> maintenanceRecordsEquipments;
}





