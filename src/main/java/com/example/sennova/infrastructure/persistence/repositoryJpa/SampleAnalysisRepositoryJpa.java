package com.example.sennova.infrastructure.persistence.repositoryJpa;

import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.SampleAnalysisEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SampleAnalysisRepositoryJpa extends JpaRepository<SampleAnalysisEntity, Long> {

}
