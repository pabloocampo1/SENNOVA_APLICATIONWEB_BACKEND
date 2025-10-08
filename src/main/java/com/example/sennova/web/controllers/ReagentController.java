package com.example.sennova.web.controllers;

import com.example.sennova.application.dto.inventory.ReagentInventory.ReagentRequestDto;
import com.example.sennova.application.dto.inventory.ReagentInventory.ReagentResponseDto;
import com.example.sennova.application.mapper.ReagentMapper;
import com.example.sennova.application.usecases.ReagentUseCase;
import com.example.sennova.domain.model.ReagentModel;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reagent")
public class ReagentController {

    private final ReagentUseCase reagentUseCase;
    private final ReagentMapper reagentMapper;

    @Autowired
    public ReagentController(ReagentUseCase reagentUseCase, ReagentMapper reagentMapper) {
        this.reagentUseCase = reagentUseCase;
        this.reagentMapper = reagentMapper;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<ReagentResponseDto>> getAll() {
        List<ReagentModel> reagentModels = this.reagentUseCase.getAll();
        return new ResponseEntity<>(reagentModels.stream().map(this.reagentMapper::toResponse).toList(), HttpStatus.OK);
    }

    @GetMapping("/getAll/page")
    public ResponseEntity<Page<ReagentResponseDto>> getAllPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int elements
    ) {
        Pageable pageable = PageRequest.of(page, elements);
        Page<ReagentModel> reagentModels = this.reagentUseCase.getAll(pageable);
        return new ResponseEntity<>(reagentModels.map(this.reagentMapper::toResponse), HttpStatus.OK);
    }

    @GetMapping("/getAllByLocation/{locationId}")
    public ResponseEntity<List<ReagentResponseDto>> getAllByLocation(@PathVariable("locationId") Long id) {
        List<ReagentModel> reagents = this.reagentUseCase.getAllByLocation(id);
        return new ResponseEntity<>(reagents.stream().map(this.reagentMapper::toResponse).toList(), HttpStatus.OK);
    }

    @GetMapping("/getAllByName/{name}")
    public ResponseEntity<List<ReagentResponseDto>> getAllByName(@PathVariable("locationId") String name) {
        List<ReagentModel> reagents = this.reagentUseCase.getAllByName(name);
        return new ResponseEntity<>(reagents.stream().map(this.reagentMapper::toResponse).toList(), HttpStatus.OK);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<ReagentResponseDto> getAllByName(@PathVariable("id") Long id) {
        ReagentModel reagent = this.reagentUseCase.getById(id);
        return new ResponseEntity<>(this.reagentMapper.toResponse(reagent), HttpStatus.OK);
    }

    @PostMapping(path = "/save/{performedBy}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReagentResponseDto> save(
            @RequestPart("dto") ReagentRequestDto reagentRequestDto,
            @RequestPart(name = "image", required = false) MultipartFile imageFile,
            @PathVariable(name = "performedBy") Long performedBy
    ) {
        System.out.println("pepepeppepe");
        try {
            ReagentModel reagentModel = this.reagentMapper.toModel(reagentRequestDto);
            ReagentModel reagentSaved = this.reagentUseCase.save(
                    reagentModel,
                    imageFile,
                    performedBy,
                    reagentRequestDto.responsibleId(),
                    reagentRequestDto.locationId(),
                    reagentRequestDto.usageId()
            );
            return new ResponseEntity<>(this.reagentMapper.toResponse(reagentSaved), HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        this.reagentUseCase.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
