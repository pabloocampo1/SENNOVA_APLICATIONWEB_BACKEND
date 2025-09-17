package com.example.sennova.application.usecasesImpl;

import com.example.sennova.application.dto.UserDtos.*;
import com.example.sennova.application.mapper.UserMapper;
import com.example.sennova.application.usecases.UserUseCase;
import com.example.sennova.domain.model.RoleModel;
import com.example.sennova.domain.model.UserModel;
import com.example.sennova.domain.port.RolePersistencePort;
import com.example.sennova.domain.port.UserPersistencePort;
import com.example.sennova.infrastructure.persistence.entities.UserEntity;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserUseCase {

    private final UserPersistencePort userPersistencePort;
    private final UserMapper userMapper;
    private final RolePersistencePort rolePersistencePort;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceImpl(UserPersistencePort userPersistencePort, UserMapper userMapper, RolePersistencePort rolePersistencePort, PasswordEncoder passwordEncoder) {
        this.userPersistencePort = userPersistencePort;
        this.userMapper = userMapper;
        this.rolePersistencePort = rolePersistencePort;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    @Transactional
    public UserResponse save(UserSaveRequest userSaveRequest) {
        try{
            UserModel userModel = this.userMapper.toModel(userSaveRequest);

            // search y  validate if the role exist.
            RoleModel roleModel = this.rolePersistencePort.findByName(userSaveRequest.roleName());

            // add the role to the user
            userModel.setRole(roleModel);

            // encrip one password by default (the user can change after)
            userModel.setPassword(passwordEncoder.encode(userModel.getDni().toString()));

            // save the user
            UserModel userSaved = this.userPersistencePort.save(userModel);

            // return the response
            return this.userMapper.toResponse(userSaved);
        }catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserResponse saveModel(UserModel userModel) {
        UserModel userModel1 = this.userPersistencePort.save(userModel);
        return this.userMapper.toResponse(userModel1);
    }

    @Override
    public List<UserResponse> findAll() {
        return this.userMapper.toResponse(this.userPersistencePort.findAll());
    }

    @Override
    public UserResponse findById(@Valid Long id) {
           UserModel userModel = this.userPersistencePort.findById(id);
           return this.userMapper.toResponse(userModel);
    }

    @Override
    @Transactional
    public UserResponse update(@Valid  Long userId, @Valid UserUpdateDto userUpdateDto) {

        if(!this.userPersistencePort.existsById(userUpdateDto.userId())){
            throw  new UsernameNotFoundException("El usuario " + userUpdateDto.name() + " no existe.");
        }

        UserModel user = new UserModel();
        user.setUserId(userUpdateDto.userId());
        user.setName(userUpdateDto.name());
        user.setDni(userUpdateDto.dni());
        user.setAvailable(userUpdateDto.available());
        user.setPhoneNumber(userUpdateDto.phoneNumber());
        user.setEmail(userUpdateDto.email());
        user.setImageProfile(userUpdateDto.imageProfile());
        user.setPosition(userUpdateDto.position());
        user.setRole(userUpdateDto.role());

        return this.userMapper.toResponse(this.userPersistencePort.update(user));
    }

    @Override
    public void deleteUser(@Valid Long userId) {
        if(!this.userPersistencePort.existsById(userId)){
            throw new UsernameNotFoundException("No se encontro ese usuario para eliminar");
        }

        this.userPersistencePort.deleteUser(userId);
    }

    @Override
    public List<UserResponse> findByName(String name) {
       List<UserResponse> userResponseList = this.userPersistencePort.findByName(name)
               .stream()
               .map(this.userMapper::toResponse)
               .toList();

        return userResponseList;
    }

    @Override
    public UserModel findByUsername(@Valid String username) {
        return this.userPersistencePort.findByUsername(username);
    }

    @Override
    public List<UserResponse> findByRole(@Valid Long roleId) {
        List<UserResponse> userResponseList = this.userPersistencePort.findByRole(roleId)
                .stream()
                .map(this.userMapper::toResponse)
                .toList();

        return userResponseList;
    }

    @Override
    public List<UserResponse> findByDni(Long dni) {
        List<UserResponse> userResponseList = this.userPersistencePort.findByDni(dni)
                .stream()
                .map(this.userMapper::toResponse)
                .toList();

        return userResponseList;
    }

    @Override
    public Boolean existByUsername(String username) {
        return this.userPersistencePort.existByUserName(username);
    }

    @Override
    public void saveRefreshToken(String refreshToken, String username) {
        this.userPersistencePort.saveRefreshToken(refreshToken,username);
    }

    @Override
    public void deleteRefreshToken( @Valid String username) {
        this.userPersistencePort.deleteRefreshToken(username);
    }

    @Override
    public UserModel getByEmail(String email) {
        return this.userPersistencePort.findByEmail(email);
    }

    @Override
    public UserPreferenceResponse changePreference(@Valid UserPreferencesRequestDto userPreferencesRequestDto, @Valid String username) {
        UserModel userModel = this.userPersistencePort.findByUsername(username);

        userModel.setNotifyEquipment(userPreferencesRequestDto.inventoryEquipment());
        userModel.setNotifyReagents(userPreferencesRequestDto.inventoryReagents());
        userModel.setNotifyQuotes(userPreferencesRequestDto.quotations());
        userModel.setNotifyResults(userPreferencesRequestDto.results());
        UserModel userUpdate = this.userPersistencePort.save(userModel);
        UserPreferenceResponse userPreferenceResponse = new UserPreferenceResponse(userModel.isNotifyEquipment(), userModel.isNotifyReagents(), userModel.isNotifyQuotes(), userModel.isNotifyResults());
        return userPreferenceResponse ;
    }

    @Override
    public String changeEmail(String currentEmail, String newEmail) {
        UserModel userModel = this.userPersistencePort.findByEmail(currentEmail);

        if(this.userPersistencePort.existByEmail(newEmail)){
            throw new IllegalArgumentException("El nuevo email no esta disponible");
        }

        userModel.setEmail(newEmail);

       UserModel userUpdated =  this.userPersistencePort.update(userModel);

        return  userUpdated.getEmail();
    }

    @Override
    public boolean existByEmail(@Valid  String email) {
        return this.userPersistencePort.existByEmail(email);
    }

    @Override
    public UserEntity getEntity(Long userId) {
        return this.userPersistencePort.findEntityById(userId);
    }
}
