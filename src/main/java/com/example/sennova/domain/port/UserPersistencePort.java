package com.example.sennova.domain.port;

import com.example.sennova.domain.model.UserModel;

import java.util.List;

public interface UserPersistencePort {
    UserModel findById(Long id);
    UserModel save(UserModel userModel);
    List<UserModel> findAll();
    UserModel update(Long userId, UserModel userModel);
    void deleteUser();
    List<UserModel> findByName(String name);
    List<UserModel> findByRole(String role);
    List<UserModel> findByDni(Long dni);
    Boolean existByUserName(String username);
    UserModel findByUsername(String username);

}
