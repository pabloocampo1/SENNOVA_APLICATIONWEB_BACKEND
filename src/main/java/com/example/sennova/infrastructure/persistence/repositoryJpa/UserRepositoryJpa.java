package com.example.sennova.infrastructure.persistence.repositoryJpa;

import com.example.sennova.infrastructure.persistence.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryJpa extends JpaRepository<UserEntity, Long> {

    boolean existsByUsername(String username);

    @Query(value = "SELECT * FROM users WHERE username = :username", nativeQuery = true)
    Optional<UserEntity> findByUsername(@Param("username") String username);

    List<UserEntity> findAllByNameContainingIgnoreCase(String name);
    List<UserEntity> findAllByDni(Long dni);

    @Query("SELECT u FROM UserEntity u WHERE u.role.roleId = :roleId")
    List<UserEntity> findAllByRole(@Param("roleId") Long roleId);

    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);

}
