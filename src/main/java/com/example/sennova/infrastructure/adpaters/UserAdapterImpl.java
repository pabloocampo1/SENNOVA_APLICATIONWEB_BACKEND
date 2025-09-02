package com.example.sennova.infrastructure.adpaters;

import com.example.sennova.application.mapper.UserMapper;
import com.example.sennova.domain.model.UserModel;
import com.example.sennova.domain.port.UserPersistencePort;
import com.example.sennova.infrastructure.mapperDbo.UserMapperDbo;
import com.example.sennova.infrastructure.persistence.entities.RoleEntity;
import com.example.sennova.infrastructure.persistence.entities.UserEntity;
import com.example.sennova.infrastructure.persistence.repositoryJpa.RoleRepositoryJpa;
import com.example.sennova.infrastructure.persistence.repositoryJpa.UserRepositoryJpa;
import com.example.sennova.web.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserAdapterImpl implements UserPersistencePort {

    private final UserRepositoryJpa userRepositoryJpa;
    private final UserMapperDbo userMapperDbo;
    private final RoleRepositoryJpa roleRepositoryJpa;

    @Autowired
    public UserAdapterImpl(UserRepositoryJpa userRepositoryJpa, UserMapperDbo userMapperDbo, RoleRepositoryJpa roleRepositoryJpa) {
        this.userRepositoryJpa = userRepositoryJpa;
        this.userMapperDbo = userMapperDbo;
        this.roleRepositoryJpa = roleRepositoryJpa;
    }


    @Override
    public UserModel findById(Long id) {
        UserEntity userEntity = this.userRepositoryJpa.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro el usuario con id : " + id));
        return this.userMapperDbo.toModel(userEntity);
    }

    @Override
    public UserModel save(UserModel userModel) {
        UserEntity userEntity = this.userMapperDbo.toEntity(userModel);
        RoleEntity role = this.roleRepositoryJpa.findByNameRole(userModel.getRole().getNameRole())
                .orElseThrow(() -> new IllegalArgumentException("El rol de " + userModel.getRole().getNameRole() + "no existe."));
        userEntity.setRole(role);
        UserEntity userSaved = this.userRepositoryJpa.save(userEntity);
        return this.userMapperDbo.toModel(userSaved);
    }

    @Override
    public List<UserModel> findAll() {
        return this.userMapperDbo.toModel(this.userRepositoryJpa.findAll());
    }

    @Override
    public UserModel update(Long userId, UserModel userModel) {
        return null;
    }

    @Override
    public void deleteUser() {

    }

    @Override
    public List<UserModel> findByName(String name) {
        return List.of();
    }

    @Override
    public List<UserModel> findByRole(String role) {
        return List.of();
    }

    @Override
    public List<UserModel> findByDni(Long dni) {
        return List.of();
    }
}
