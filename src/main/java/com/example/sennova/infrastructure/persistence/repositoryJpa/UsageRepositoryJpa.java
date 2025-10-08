package com.example.sennova.infrastructure.persistence.repositoryJpa;

import com.example.sennova.infrastructure.persistence.entities.UsageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsageRepositoryJpa extends JpaRepository<UsageEntity, Long> {

    List<UsageEntity> findAllByUsageNameContainingIgnoreCase(String name);
}
