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

import org.springframework.security.access.prepost.PreAuthorize;
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
    public UserModel update(UserModel userModel) {
        UserEntity userWithDataImportant = this.userRepositoryJpa.findById(userModel.getUserId()).orElseThrow();
        UserEntity userEntity = this.userMapperDbo.toEntity(userModel);

        userEntity.setPassword(userWithDataImportant.getPassword());
        userEntity.setUsername(userWithDataImportant.getUsername());
        UserEntity userUpdated = this.userRepositoryJpa.save(userEntity);

        return  this.userMapperDbo.toModel(userUpdated);
    }

    @Override
    public void deleteUser(Long userId) {
        this.userRepositoryJpa.deleteById(userId);
    }

    @Override
    public List<UserModel> findByName(String name) {
        List<UserEntity> userEntityList = this.userRepositoryJpa.findAllByNameContainingIgnoreCase(name);

        return userEntityList.stream().map(this.userMapperDbo::toModel).toList();
    }

    @Override
    public List<UserModel> findByRole(Long roleId) {
        List<UserEntity> userEntityList = this.userRepositoryJpa.findAllByRole(roleId);
        return userEntityList.stream().map(this.userMapperDbo::toModel).toList();
    }

    @Override
    public List<UserModel> findByDni(Long dni) {
        List<UserEntity> userEntityList = this.userRepositoryJpa.findAllByDni(dni);
        return userEntityList.stream().map(this.userMapperDbo::toModel).toList();
    }

    @Override
    public Boolean existByUserName(String username) {
        return this.userRepositoryJpa.existsByUsername(username);
    }

    @Override
    public Boolean existsById(Long id) {
        return this.userRepositoryJpa.existsById(id);
    }

    @Override
    public UserModel findByUsername(String username) {

        UserEntity user = this.userRepositoryJpa.findByUsername(username).orElseThrow();
        return this.userMapperDbo.toModel(user);
    }

    @Override
    public void saveRefreshToken(String refreshToken, String username) {
        System.out.println(username);
        UserEntity user = this.userRepositoryJpa.findByUsername(username)
                .orElseThrow();

        user.setRefreshToken(refreshToken);
        this.userRepositoryJpa.save(user);
    }

    @Override
    public void deleteRefreshToken(String username) {
        UserEntity user = this.userRepositoryJpa.findByUsername(username)
                .orElseThrow();

        user.setRefreshToken(null);
        this.userRepositoryJpa.save(user);
    }
}
