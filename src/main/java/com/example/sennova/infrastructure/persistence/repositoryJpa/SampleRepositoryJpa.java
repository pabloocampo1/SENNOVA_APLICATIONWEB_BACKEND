package com.example.sennova.infrastructure.persistence.repositoryJpa;

import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.SampleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SampleRepositoryJpa extends JpaRepository<SampleEntity, Long> {
}
