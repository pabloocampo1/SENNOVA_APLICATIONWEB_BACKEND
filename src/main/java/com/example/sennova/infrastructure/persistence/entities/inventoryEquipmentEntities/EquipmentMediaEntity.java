package com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities;


import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Table(name = "equipment_media")
@Data
@EntityListeners(AuditingEntityListener.class)
public class EquipmentMediaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long equipmentMediaId;

    @Column(nullable = false)
    private String url;

    private String type;

    @CreatedDate
    private LocalDate createAt;

    @LastModifiedDate
    private LocalDate updateAt;
}
