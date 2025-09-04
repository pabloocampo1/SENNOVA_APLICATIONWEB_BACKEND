package com.example.sennova.application.mapper;

import com.example.sennova.application.dto.UserDtos.UserResponse;
import com.example.sennova.application.dto.UserDtos.UserSaveRequest;
import com.example.sennova.domain.model.UserModel;
import com.example.sennova.infrastructure.persistence.entities.UserEntity;
import org.hibernate.annotations.Comments;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(UserModel userModel);
    UserModel toModel(UserSaveRequest userSaveRequest);

    List<UserResponse> toResponse(Iterable<UserModel> userModelIterable);

}
