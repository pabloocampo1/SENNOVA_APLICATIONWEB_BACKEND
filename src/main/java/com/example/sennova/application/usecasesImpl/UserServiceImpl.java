package com.example.sennova.application.usecasesImpl;

import com.example.sennova.application.dto.UserDtos.UserResponse;
import com.example.sennova.application.dto.UserDtos.UserSaveRequest;
import com.example.sennova.application.mapper.UserMapper;
import com.example.sennova.application.usecases.UserUseCase;
import com.example.sennova.domain.model.RoleModel;
import com.example.sennova.domain.model.UserModel;
import com.example.sennova.domain.port.RolePersistencePort;
import com.example.sennova.domain.port.UserPersistencePort;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserUseCase {

    private final UserPersistencePort userPersistencePort;
    private final UserMapper userMapper;
    private final RolePersistencePort rolePersistencePort;


    @Autowired
    public UserServiceImpl(UserPersistencePort userPersistencePort, UserMapper userMapper, RolePersistencePort rolePersistencePort) {
        this.userPersistencePort = userPersistencePort;
        this.userMapper = userMapper;
        this.rolePersistencePort = rolePersistencePort;
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
    public List<UserResponse> findAll() {
        return this.userMapper.toResponse(this.userPersistencePort.findAll());
    }

    @Override
    public UserResponse findById(@Valid Long id) {

           UserModel userModel = this.userPersistencePort.findById(id);
           return this.userMapper.toResponse(userModel);
    }

    @Override
    public UserResponse update(Long userId, UserSaveRequest userSaveRequest) {
        return null;
    }

    @Override
    public void deleteUser() {

    }

    @Override
    public List<UserResponse> findByName(String name) {
        return List.of();
    }

    @Override
    public List<UserResponse> findByRole(String role) {
        return List.of();
    }

    @Override
    public List<UserResponse> findByDni(Long dni) {
        return List.of();
    }
}
