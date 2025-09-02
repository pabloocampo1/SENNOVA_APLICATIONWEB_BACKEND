package com.example.sennova.infrastructure.persistence.entities.inventoryReagentsEntities;

import com.example.sennova.infrastructure.persistence.entities.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.List;

@Table(name = "reagents")
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
public class ReagentsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reagents_id")
    private Long reagentsId;

    @Column(nullable = false)
    private String reagent_name;

    private String brand;

    private String purity;

    @Column(nullable = false)
    private Integer units;

    @Column(nullable = false)
    private Integer quantity;

    private String measurement_unit;

    private String batch;

    @Column(nullable = false)
    private LocalDate expiration_date;

    @CreatedDate
    private LocalDate createAt;

    @LastModifiedDate
    private LocalDate updateAt;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "usage_reagent_id", referencedColumnName = "usage_reagent_id")
    private UsageReagentsEntity usage;

    @ManyToOne
    @JoinColumn(name = "location_reagent_id", referencedColumnName = "location_reagent_id")
    private LocationReagents location;

    @OneToMany(mappedBy = "reagent")
    private List<ReagentsUsageRecords> reagentsUsageRecordsList;
}
