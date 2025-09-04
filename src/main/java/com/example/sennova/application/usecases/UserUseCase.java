package com.example.sennova.application.usecases;

import com.example.sennova.application.dto.UserDtos.UserResponse;
import com.example.sennova.application.dto.UserDtos.UserSaveRequest;
import com.example.sennova.application.dto.UserDtos.UserUpdateDto;
import com.example.sennova.domain.model.UserModel;

import java.util.List;

public interface UserUseCase {
    UserResponse save(UserSaveRequest userSaveRequest);
    List<UserResponse> findAll();
    UserResponse findById(Long id);
    UserResponse update(Long userId, UserUpdateDto userUpdateDto);
    void deleteUser(Long userId);
    List<UserResponse> findByName(String name);
    UserModel findByUsername(String username);
    List<UserResponse> findByRole(Long roleId);
    List<UserResponse> findByDni(Long dni);
    Boolean existByUsername(String username);
    void saveRefreshToken(String refreshToken, String username);
    void deleteRefreshToken(String username);
}
