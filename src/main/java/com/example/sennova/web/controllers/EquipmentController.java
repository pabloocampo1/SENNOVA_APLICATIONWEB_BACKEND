package com.example.sennova.web.controllers;

import com.example.sennova.application.dto.EquipmentInventory.EquipmentRequestDto;
import com.example.sennova.application.dto.EquipmentInventory.EquipmentResponseDto;
import com.example.sennova.application.mapper.EquipmentMapper;
import com.example.sennova.application.usecases.EquipmentUseCase;
import com.example.sennova.domain.model.EquipmentModel;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/v1/equipment")
public class EquipmentController {

    private final EquipmentMapper equipmentMapper;
    private final EquipmentUseCase equipmentUseCase;

    @Autowired
    public EquipmentController(EquipmentMapper equipmentMapper, EquipmentUseCase equipmentUseCase) {
        this.equipmentMapper = equipmentMapper;
        this.equipmentUseCase = equipmentUseCase;
    }
    @PostMapping("/save")
    public ResponseEntity<EquipmentResponseDto> save(@RequestBody @Valid EquipmentRequestDto equipmentRequestDto) {

        EquipmentModel equipmentToSave = this.equipmentMapper.toDomain(equipmentRequestDto);

        EquipmentModel equipmentModelSaved = this.equipmentUseCase.save(
                equipmentToSave,
                equipmentRequestDto.responsibleId(),
                equipmentRequestDto.locationId(),
                equipmentRequestDto.usageId()
        );

        EquipmentResponseDto response = new EquipmentResponseDto(
                equipmentModelSaved.getEquipmentId(),
                equipmentModelSaved.getInternalCode(),
                equipmentModelSaved.getEquipmentName(),
                equipmentModelSaved.getBrand(),
                equipmentModelSaved.getModel(),
                equipmentModelSaved.getSerialNumber(),
                equipmentModelSaved.getAcquisitionDate(),
                equipmentModelSaved.getMaintenanceDate(),
                equipmentModelSaved.getAmperage(),
                equipmentModelSaved.getVoltage(),
                equipmentModelSaved.getEquipmentCost(),
                equipmentModelSaved.getState(),
                equipmentModelSaved.getAvailable(),
                equipmentModelSaved.getResponsible() != null ? equipmentModelSaved.getResponsible().getUserId() : null,
                equipmentModelSaved.getResponsible() != null ? equipmentModelSaved.getResponsible().getName() : null,
                equipmentModelSaved.getLocation() != null ? equipmentModelSaved.getLocation().getEquipmentLocationId() : null,
                equipmentModelSaved.getLocation() != null ? equipmentModelSaved.getLocation().getLocationName() : null,
                equipmentModelSaved.getUsage() != null ? equipmentModelSaved.getUsage().getEquipmentUsageId() : null,
                equipmentModelSaved.getUsage() != null ? equipmentModelSaved.getUsage().getUsageName() : null
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }






}


