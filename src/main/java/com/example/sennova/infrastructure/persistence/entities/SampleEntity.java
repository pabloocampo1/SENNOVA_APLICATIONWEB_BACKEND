package com.example.sennova.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.boot.context.properties.bind.Name;
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
    @Column(nullable = false, unique = true, length = 200, name = "sample_code")
    private String sampleCode;

    @Column(nullable = false)
    private String matrix;

    @Column(length = 300)
    private String description;

    @CreatedDate
    private LocalDate createAt;

    @LastModifiedDate
    private LocalDate updateAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_request_code", referencedColumnName = "request_code", nullable = false)
    private TestRequestEntity test_request;

    @OneToMany(mappedBy = "sample")
    private List<SampleProductAnalysisEntity> analysisEntities;

    @OneToOne(mappedBy = "sample")
    private SampleReception reception;


}
