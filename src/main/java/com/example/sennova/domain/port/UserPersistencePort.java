package com.example.sennova.domain.port;

import com.example.sennova.domain.model.UserModel;

import java.util.List;

public interface UserPersistencePort {
    UserModel findById(Long id);
    UserModel save(UserModel userModel);
    List<UserModel> findAll();
    UserModel update(UserModel userModel);
    void deleteUser(Long userId);
    List<UserModel> findByName(String name);
    List<UserModel> findByRole(Long roleId);
    List<UserModel> findByDni(Long dni);
    Boolean existByUserName(String username);
    Boolean existsById(Long id);
    UserModel findByUsername(String username);
    void saveRefreshToken(String refreshToken, String username);
    void deleteRefreshToken(String username);

}
