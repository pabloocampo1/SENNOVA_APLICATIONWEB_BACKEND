package com.example.sennova.infrastructure.persistence.entities.inventoryEquipment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Table(name = "equipment_loan")
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
public class EquipmentLoanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "equipment_loan_id")
    private Long equipmentLoanId;

    private String loanPurpose;

    @Column(nullable = false)
    private String username;

    @Column(length = 400)
    private String notes;

    @CreatedDate
    private LocalDate createAt;

    @LastModifiedDate
    private LocalDate updateAt;

    @ManyToOne()
    @JoinColumn(name = "equipment_id", referencedColumnName = "equipment_id")
    private EquipmentEntity equipment;

}
