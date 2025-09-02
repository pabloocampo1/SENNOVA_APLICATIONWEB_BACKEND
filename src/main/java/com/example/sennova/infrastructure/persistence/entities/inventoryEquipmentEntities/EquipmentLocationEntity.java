package com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "equipment_location")
@Data
@EntityListeners(AuditingEntityListener.class)
public class EquipmentLocationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="equipment_location_id" )
    private Long equipmentLocationId;

    @Column(nullable = false)
    private String location_name;

    @CreatedDate
    private LocalDate createAt;

    @LastModifiedDate
    private LocalDate updateAt;

    @OneToMany(mappedBy = "location")
    private List<EquipmentEntity> equipmentEntityList;
}
