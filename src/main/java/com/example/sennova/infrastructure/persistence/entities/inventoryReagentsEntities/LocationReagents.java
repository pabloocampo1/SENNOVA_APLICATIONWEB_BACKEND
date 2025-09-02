package com.example.sennova.infrastructure.persistence.entities.inventoryReagentsEntities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Table(name = "location_reagents")
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
public class LocationReagents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_reagent_id")
    private Long locationReagentId;

    @Column(nullable = false)
    private String zone;

    private String notes;

    @OneToMany(mappedBy = "location")
    private List<ReagentsEntity> reagentsEntityList;
}
