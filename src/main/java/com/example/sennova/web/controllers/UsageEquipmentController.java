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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usage/equipment")
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
        EquipmentUsageModel usageModelSaved = this.usageEquipmentUseCase.update(id, equipmentLocationModel);
        return new ResponseEntity<>(this.equipmentUsageMapper.toResponse(usageModelSaved), HttpStatus.OK);
    }

    @GetMapping("/getAllByName/{name}")
    public ResponseEntity<List<UsageEquipmentResponseDto>> getAllByName(@PathVariable("name") String name){
        return new ResponseEntity<>(this.usageEquipmentUseCase.getAllByName(name).stream().map(this.equipmentUsageMapper::toResponse).toList(), HttpStatus.OK);
    }



    @GetMapping("/getAll")
    public ResponseEntity<List<UsageEquipmentResponseDto>> getAll(){
       try{
           return new ResponseEntity<>(this.usageEquipmentUseCase.getAll().stream().map(this.equipmentUsageMapper::toResponse).toList(), HttpStatus.OK);
       } catch (Exception e) {
           e.printStackTrace();
           throw new RuntimeException(e);
       }
    }

    @GetMapping("/getAllPage")
    public ResponseEntity<Page<UsageEquipmentResponseDto>> getAllPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int elements
    ){
        Pageable pageable = PageRequest.of(page, elements, Sort.by("createAt").descending() );
        return new ResponseEntity<>(this.usageEquipmentUseCase.getAll(pageable).map(this.equipmentUsageMapper::toResponse), HttpStatus.OK);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<UsageEquipmentResponseDto> getById(@PathVariable("id") Long id){
        return new ResponseEntity<>(this.equipmentUsageMapper.toResponse(this.usageEquipmentUseCase.getById(id)), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id){
        this.usageEquipmentUseCase.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
