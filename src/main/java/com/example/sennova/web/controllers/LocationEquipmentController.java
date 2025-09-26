package com.example.sennova.web.controllers;

import com.example.sennova.application.dto.EquipmentInventory.request.LocationEquipmentRequestDto;
import com.example.sennova.application.dto.EquipmentInventory.response.LocationEquipmentResponseDto;
import com.example.sennova.application.mapper.EquipmentLocationMapper;
import com.example.sennova.application.usecases.LocationEquipmentUseCase;
import com.example.sennova.domain.model.EquipmentLocationModel;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/location")
public class LocationEquipmentController {
    private final LocationEquipmentUseCase locationEquipmentUseCase;
    private final EquipmentLocationMapper equipmentLocationMapper;

    @Autowired
    public LocationEquipmentController(LocationEquipmentUseCase locationEquipmentUseCase, EquipmentLocationMapper equipmentLocationMapper) {
        this.locationEquipmentUseCase = locationEquipmentUseCase;
        this.equipmentLocationMapper = equipmentLocationMapper;
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<LocationEquipmentResponseDto> getById(@PathVariable("id") @Valid Long id){
        return new ResponseEntity<>(this.equipmentLocationMapper.toResponse(this.locationEquipmentUseCase.getById(id)),HttpStatus.OK );
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<LocationEquipmentResponseDto>> getAll(){
        List<EquipmentLocationModel> all = this.locationEquipmentUseCase.getAll();
        return new ResponseEntity<>(all.stream().map(this.equipmentLocationMapper::toResponse).toList(),HttpStatus.OK );
    }

    @GetMapping("/getByName/{name}")
    public ResponseEntity<List<LocationEquipmentResponseDto>> getById(@PathVariable("name") @Valid String name){
        return new ResponseEntity<>(
                this.locationEquipmentUseCase.getAllByName(name).stream().map(this.equipmentLocationMapper::toResponse).toList(),
                HttpStatus.OK );
    }


    @PostMapping("/save")
    public ResponseEntity<LocationEquipmentResponseDto> save(@RequestBody @Valid LocationEquipmentRequestDto locationEquipmentRequestDto) {
        EquipmentLocationModel equipmentLocationModel = this.equipmentLocationMapper.toModel(locationEquipmentRequestDto);
        EquipmentLocationModel locationModelSaved = this.locationEquipmentUseCase.save(equipmentLocationModel);
        return new ResponseEntity<>(this.equipmentLocationMapper.toResponse(locationModelSaved), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<LocationEquipmentResponseDto> update(
            @RequestBody @Valid LocationEquipmentRequestDto locationEquipmentRequestDto,
            @PathVariable("id") @Valid Long id) {
        EquipmentLocationModel equipmentLocationModel = this.equipmentLocationMapper.toModel(locationEquipmentRequestDto);
        EquipmentLocationModel locationModelSaved = this.locationEquipmentUseCase.update( id, equipmentLocationModel);
        return new ResponseEntity<>(this.equipmentLocationMapper.toResponse(locationModelSaved), HttpStatus.OK);
    }

    @GetMapping("/getAllPage")
    public ResponseEntity<Page<LocationEquipmentResponseDto>> getAll(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "6")  int elements
    ){
        Pageable pageable = PageRequest.of(page, elements, Sort.by("createAt").descending());
        Page<EquipmentLocationModel> locationModelPage = this.locationEquipmentUseCase.getAllPage(pageable);

        Page<LocationEquipmentResponseDto> pageToReturn = locationModelPage.map(this.equipmentLocationMapper::toResponse);
        return new ResponseEntity<>(pageToReturn,HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id){
        this.locationEquipmentUseCase.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
