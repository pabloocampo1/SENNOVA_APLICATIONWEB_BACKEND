package com.example.sennova.application.usecases;

import com.example.sennova.application.dto.UserDtos.UserResponse;
import com.example.sennova.application.dto.UserDtos.UserSaveRequest;
import com.example.sennova.domain.model.UserModel;

import java.util.List;

public interface UserUseCase {
    UserResponse save(UserSaveRequest userSaveRequest); // listoooo
    List<UserResponse> findAll(); // LISTOOOOO
    UserResponse findById(Long id);
    UserResponse update(Long userId, UserSaveRequest userSaveRequest);
    void deleteUser();
    List<UserResponse> findByName(String name);
    List<UserResponse> findByRole(String role);
    List<UserResponse> findByDni(Long dni);
}
