package com.example.sennova.infrastructure.persistence.entities.analysisRequests;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Table(name = "Sample_reception")
@Data
@EntityListeners(AuditingEntityListener.class)
public class SampleReception {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sampleReceptionId;

    @Column(nullable = false)
    private LocalDate sampling_time;

    @Column(nullable = false)
    private LocalDate sampling_date;

    @Column(nullable = false)
    private double gross_weight;

    @Column(nullable = false)
    private double temperature;

    @Column(nullable = false)
    private String package_description;

    @Column(nullable = false)
    private String storage_conditions;

    @Column(nullable = false)
    private String observations;

    private Boolean status;

    private String sampleImage;

    @CreatedDate
    private LocalDate createAt;

    @OneToOne
    @JoinColumn(name = "sample_code", referencedColumnName = "sample_code")
    private SampleEntity sample;

    @LastModifiedDate
    private LocalDate updateAt;

}
