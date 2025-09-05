package com.example.sennova.web.controllers;

import com.example.sennova.application.dto.EquipmentInventory.LocationEquipmentRequestDto;
import com.example.sennova.application.dto.EquipmentInventory.LocationEquipmentResponseDto;
import com.example.sennova.application.dto.EquipmentInventory.UsageEquipmentRequestDto;
import com.example.sennova.application.dto.EquipmentInventory.UsageEquipmentResponseDto;
import com.example.sennova.application.mapper.EquipmentUsageMapper;
import com.example.sennova.application.usecases.LocationEquipmentUseCase;
import com.example.sennova.application.usecases.UsageEquipmentUseCase;
import com.example.sennova.domain.model.EquipmentLocationModel;
import com.example.sennova.domain.model.EquipmentUsageModel;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/usage")
public class UsageEquipmentController {

    private final UsageEquipmentUseCase usageEquipmentUseCase;
    private final EquipmentUsageMapper equipmentUsageMapper;

    @Autowired
    public UsageEquipmentController(UsageEquipmentUseCase usageEquipmentUseCase, EquipmentUsageMapper equipmentUsageMapper) {
        this.usageEquipmentUseCase = usageEquipmentUseCase;
        this.equipmentUsageMapper = equipmentUsageMapper;
    }


    @PostMapping("/save")
    public ResponseEntity<UsageEquipmentResponseDto> save(@RequestBody @Valid UsageEquipmentRequestDto usageEquipmentRequestDto){
        EquipmentUsageModel equipmentLocationModel = this.equipmentUsageMapper.toModel(usageEquipmentRequestDto);
        EquipmentUsageModel usageModelSaved = this.usageEquipmentUseCase.save(equipmentLocationModel);
        return new ResponseEntity<>(this.equipmentUsageMapper.toResponse(usageModelSaved), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UsageEquipmentResponseDto> update(
            @RequestBody @Valid UsageEquipmentRequestDto usageEquipmentRequestDto,
            @PathVariable("id") @Valid Long id) {
        EquipmentUsageModel equipmentLocationModel = this.equipmentUsageMapper.toModel(usageEquipmentRequestDto);
        EquipmentUsageModel usageModelSaved = this.usageEquipmentUseCase.save(equipmentLocationModel);
        return new ResponseEntity<>(this.equipmentUsageMapper.toResponse(usageModelSaved), HttpStatus.OK);
    }



}
