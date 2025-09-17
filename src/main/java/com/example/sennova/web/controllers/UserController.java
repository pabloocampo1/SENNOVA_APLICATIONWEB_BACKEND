package com.example.sennova.web.controllers;

import com.example.sennova.application.dto.UserDtos.*;
import com.example.sennova.application.usecases.UserUseCase;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @GetMapping(path = "/getByName/{name}")
    public ResponseEntity<List<UserResponse>> findByName(@PathVariable("name") @Valid String name){
        return new ResponseEntity<>(this.userUseCase.findByName(name), HttpStatus.OK);
    }

    @GetMapping(path = "/getByDni/{dni}")
    public ResponseEntity<List<UserResponse>> findByDni(@PathVariable("dni") @Valid Long dni){
        return new ResponseEntity<>(this.userUseCase.findByDni(dni), HttpStatus.OK);
    }

    @GetMapping(path = "/getByRole/{roleId}")
    public ResponseEntity<List<UserResponse>> findByRole(@PathVariable("roleId") @Valid Long roleId){
        return new ResponseEntity<>(this.userUseCase.findByRole(roleId), HttpStatus.OK);
    }

    @PostMapping(path = "/save")
    public ResponseEntity<UserResponse> saveUser(@RequestBody @Valid UserSaveRequest userSaveRequest) {
        return new ResponseEntity<>(this.userUseCase.save(userSaveRequest), HttpStatus.OK);

    }

    @PutMapping(path = "/update/{userId}")
    public ResponseEntity<UserResponse> update(@RequestBody @Valid UserUpdateDto userUpdateDto, @PathVariable("userId") Long userId) {
        return new ResponseEntity<>(this.userUseCase.update(userId, userUpdateDto), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") Long userId ){
        try{
            this.userUseCase.deleteUser(userId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
           return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/changePreferences/{username}")
    public ResponseEntity<UserPreferenceResponse> changePreferences(@RequestBody UserPreferencesRequestDto userPreferencesRequestDto, @PathVariable("username") String username) {
        return new ResponseEntity<>(this.userUseCase.changePreference(userPreferencesRequestDto, username), HttpStatus.OK);
    }


}
