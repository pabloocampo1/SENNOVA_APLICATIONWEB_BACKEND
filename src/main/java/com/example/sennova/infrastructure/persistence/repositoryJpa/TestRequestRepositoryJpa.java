package com.example.sennova.infrastructure.persistence.repositoryJpa;

import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.TestRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRequestRepositoryJpa extends JpaRepository<TestRequestEntity, Long> {
}
