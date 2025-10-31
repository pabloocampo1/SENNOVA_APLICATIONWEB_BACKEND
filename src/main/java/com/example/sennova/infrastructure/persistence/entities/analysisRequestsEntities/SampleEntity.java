package com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "sample")
@Data
@EntityListeners(AuditingEntityListener.class)
public class SampleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true, name = "sample_id")
    private Long sampleId;

    @Column(nullable = false)
    private String matrix;

    @Lob
    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;

    @Column(nullable = false, unique = true)
    private String sampleCode;

    private LocalDate sampling_time;

    private LocalDate sampling_date;

    private double gross_weight;

    private double temperature;

    private String package_description;

    private String storage_conditions;

    private String observations;

    private Boolean statusReception;

    private String sampleImage;

    @CreatedDate
    private LocalDate createAt;

    @LastModifiedDate
    private LocalDate updateAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_request_id", referencedColumnName = "test_request_id", nullable = false)
    private TestRequestEntity testRequest;

    @OneToMany(mappedBy = "sample")
    private List<SampleAnalysisEntity> analysisEntities;

}
