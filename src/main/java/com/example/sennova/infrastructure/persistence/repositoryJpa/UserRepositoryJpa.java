package com.example.sennova.infrastructure.persistence.repositoryJpa;

import com.example.sennova.infrastructure.persistence.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepositoryJpa extends JpaRepository<UserEntity, Long> {

    boolean existsByUsername(String username);

    @Query(value = "SELECT * FROM users WHERE username = :username", nativeQuery = true)
    Optional<UserEntity> findByUsername(@Param("username") String username);

}
