package com.example.sennova.infrastructure.persistence.entities.inventoryReagentsEntities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Table(name = "reagent_usage_records")
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
public class ReagentsUsageRecords {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reagents_usage_records_id")
    private Long reagentUsageRecordsId;

    @Column(nullable = false)
    private String usedBy;

    @Column(nullable = false)
    private Integer quantity_used;

    @Column(length = 400)
    private String notes;

    @CreatedDate
    private LocalDate createAt;

    @LastModifiedDate
    private LocalDate updateAt;

    @ManyToOne
    @JoinColumn(name = "reagents_id", referencedColumnName = "reagents_id")
    private ReagentsEntity reagent;
}
