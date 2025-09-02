package com.example.sennova.infrastructure.persistence.repositoryJpa;

import com.example.sennova.infrastructure.persistence.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositoryJpa extends JpaRepository<UserEntity, Long> {
}
