package com.example.sennova.web.controllers;

import com.example.sennova.application.dto.EquipmentInventory.EquipmentRequestDto;
import com.example.sennova.application.dto.EquipmentInventory.EquipmentResponseDto;
import com.example.sennova.application.mapper.EquipmentMapper;
import com.example.sennova.application.usecases.EquipmentUseCase;
import com.example.sennova.domain.model.EquipmentModel;
import jakarta.validation.Valid;
import org.hibernate.query.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

        System.out.println(equipmentToSave);

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
                equipmentModelSaved.getUsage() != null ? equipmentModelSaved.getUsage().getUsageName() : null,
                equipmentModelSaved.getCreateAt(),
                equipmentModelSaved.getUpdateAt()
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<EquipmentResponseDto> update(@RequestBody @Valid EquipmentRequestDto equipmentRequestDto, @PathVariable("id") Long id) {
        EquipmentModel equipmentToSave = this.equipmentMapper.toDomain(equipmentRequestDto);

        EquipmentModel equipmentModelSaved = this.equipmentUseCase.update(
                equipmentToSave,
                id,
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
                equipmentModelSaved.getUsage() != null ? equipmentModelSaved.getUsage().getUsageName() : null,
                equipmentModelSaved.getCreateAt(),
                equipmentModelSaved.getUpdateAt()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/get-all")
    public ResponseEntity<List<EquipmentResponseDto>> getAll() {
        List<EquipmentModel> equipmentModelList = this.equipmentUseCase.getAll();
        return new ResponseEntity<>(
                equipmentModelList.stream().map(this.equipmentMapper::toResponse).toList(),
                HttpStatus.OK);
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<EquipmentResponseDto> getById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(
                this.equipmentMapper.toResponse(this.equipmentUseCase.getById(id)),
                HttpStatus.OK);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<EquipmentResponseDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int elements) {
        Pageable pageable = PageRequest.of(page, elements, Sort.by("createAt").descending());
        Page<EquipmentModel> equipmentModelPage = this.equipmentUseCase.getAll(pageable);
        Page<EquipmentResponseDto> equipmentResponseDtoPage = equipmentModelPage.map(this.equipmentMapper::toResponse);
        return new ResponseEntity<>(
                equipmentResponseDtoPage,
                HttpStatus.OK);
    }


    @GetMapping("/get-all-by-internal-code/{code}")
    public ResponseEntity<List<EquipmentResponseDto>> getAllByInternalCode(@PathVariable("code") String code) {
        List<EquipmentModel> equipmentModelList = this.equipmentUseCase.getAllByInternalCode(code);
        return new ResponseEntity<>(
                equipmentModelList.stream().map(this.equipmentMapper::toResponse).toList(),
                HttpStatus.OK);
    }


    @GetMapping("/get-all-by-name/{name}")
    public ResponseEntity<List<EquipmentResponseDto>> getAllByName(@PathVariable("name") String name) {
        List<EquipmentModel> equipmentModelList = this.equipmentUseCase.getAllByName(name);

        return new ResponseEntity<>(
                equipmentModelList.stream().map(this.equipmentMapper::toResponse).toList(),
                HttpStatus.OK);
    }


    @GetMapping("/get-all-by-location/{locationId}")
    public ResponseEntity<List<EquipmentResponseDto>> getAllByLocation(@PathVariable("locationId") Long locationId) {
        List<EquipmentModel> equipmentModelList = this.equipmentUseCase.getByLocation(locationId);
        return new ResponseEntity<>(
                equipmentModelList.stream().map(this.equipmentMapper::toResponse).toList(),
                HttpStatus.OK);
    }

    @GetMapping("/get-all-by-usage/{UsageId}")
    public ResponseEntity<List<EquipmentResponseDto>> getAllByUsage(@PathVariable("UsageId") Long UsageId) {
        List<EquipmentModel> equipmentModelList = this.equipmentUseCase.getByUsage(UsageId);
        return new ResponseEntity<>(
                equipmentModelList.stream().map(this.equipmentMapper::toResponse).toList(),
                HttpStatus.OK);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Long id) {
        this.equipmentUseCase.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/change-status/{id}/{state}")
    public ResponseEntity<Void> changeState(@PathVariable("id") Long id, @PathVariable("state") String state) {
        this.equipmentUseCase.changeState(id, state);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}


