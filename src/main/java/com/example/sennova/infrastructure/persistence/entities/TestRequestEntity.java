package com.example.sennova.infrastructure.persistence.entities;


import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "test_request")
@Data
@EntityListeners(AuditingEntityListener.class)
public class TestRequestEntity {

    @Id
    @Column(nullable = false, unique = true, length = 100, name = "request_code")
    private Long requestCode;

    @Column(nullable = false)
    private LocalDate testDate;

    @Column(nullable = false)
    private LocalDate Approval_date;

    private LocalDate discard_date;

    private double price;

    private Boolean status;

    @CreatedDate
    private LocalDate createAt;

    @LastModifiedDate
    private LocalDate updateAt;

    @Column(length = 500)
    private String notes;

    // relationships

    @OneToMany(mappedBy = "test_request")
    private List<SampleEntity> sampleEntityList;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id", nullable = false)
    private CustomerEntity customer;
}
