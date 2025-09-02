package com.example.sennova.web.controllers;

import com.example.sennova.application.dto.UserDtos.UserResponse;
import com.example.sennova.application.dto.UserDtos.UserSaveRequest;
import com.example.sennova.application.usecases.UserUseCase;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserUseCase userUseCase;

    @Autowired
    public UserController(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    @GetMapping(path = "/getAll")
    public ResponseEntity<List<UserResponse>> getAll(){
        return new ResponseEntity<>(this.userUseCase.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/getById/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable("id") @Valid Long id){
        return new ResponseEntity<>(this.userUseCase.findById(id), HttpStatus.OK);
    }

    @PostMapping(path = "/save")
    public ResponseEntity<UserResponse> saveUser(@RequestBody @Valid UserSaveRequest userSaveRequest) {
        return new ResponseEntity<>(this.userUseCase.save(userSaveRequest), HttpStatus.OK);

    }


}
