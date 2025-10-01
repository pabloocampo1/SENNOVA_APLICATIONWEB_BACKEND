package com.example.sennova.web.controllers;

import com.example.sennova.application.dto.EquipmentInventory.request.EquipmentRequestDto;
import com.example.sennova.application.dto.EquipmentInventory.response.EquipmentResponseDto;
import com.example.sennova.application.dto.EquipmentInventory.response.EquipmentStatisticsSummaryCardResponse;
import com.example.sennova.application.mapper.EquipmentMapper;
import com.example.sennova.application.usecases.EquipmentUseCase;
import com.example.sennova.domain.model.EquipmentModel;
import com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities.EquipmentMediaEntity;
import com.example.sennova.infrastructure.restTemplate.CloudinaryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/api/v1/equipment")
public class EquipmentController {

    private final EquipmentMapper equipmentMapper;
    private final EquipmentUseCase equipmentUseCase;
    private final CloudinaryService cloudinaryService;

    @Autowired
    public EquipmentController(EquipmentMapper equipmentMapper, EquipmentUseCase equipmentUseCase, CloudinaryService cloudinaryService) {
        this.equipmentMapper = equipmentMapper;
        this.equipmentUseCase = equipmentUseCase;
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EquipmentResponseDto> save(@RequestPart("dto") @Valid EquipmentRequestDto equipmentRequestDto,
                                                     @RequestPart(value = "image", required = false) MultipartFile image) {


        EquipmentModel equipmentToSave = this.equipmentMapper.toDomain(equipmentRequestDto);
        equipmentToSave.setImageUrl(null);
        EquipmentModel equipmentModelSaved = this.equipmentUseCase.save(
                equipmentToSave,
                equipmentRequestDto.responsibleId(),
                equipmentRequestDto.locationId(),
                equipmentRequestDto.usageId()
        );
        if (image != null && !image.isEmpty()) {
            try {
                String responseImage = this.cloudinaryService.uploadImage(image);
                equipmentModelSaved.setImageUrl(responseImage);
                this.equipmentUseCase.update(equipmentModelSaved, equipmentModelSaved.getEquipmentId(), equipmentModelSaved.getResponsible().getUserId(), equipmentModelSaved.getLocation().getEquipmentLocationId(), equipmentModelSaved.getUsage().getEquipmentUsageId());
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        EquipmentResponseDto response = new EquipmentResponseDto(
                equipmentModelSaved.getEquipmentId(),
                equipmentModelSaved.getInternalCode(),
                equipmentModelSaved.getEquipmentName(),
                equipmentModelSaved.getBrand(),
                equipmentModelSaved.getModel(),
                equipmentModelSaved.getSerialNumber(),
                equipmentModelSaved.getAcquisitionDate(),
                equipmentModelSaved.getMaintenanceDate(),
                equipmentModelSaved.getSenaInventoryTag(),
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
                equipmentModelSaved.getUpdateAt(),
                equipmentModelSaved.getImageUrl(),

                equipmentModelSaved.getDescription() != null ? equipmentModelSaved.getDescription() : "No hay descripcion para est equipo"
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);


    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EquipmentResponseDto> update(
            @RequestPart("dto") @Valid EquipmentRequestDto equipmentRequestDto,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @PathVariable("id") Long id) {

        EquipmentModel currentEquipment = this.equipmentUseCase.getById(id);

        EquipmentModel equipmentToUpdate = this.equipmentMapper.toDomain(equipmentRequestDto);
        equipmentToUpdate.setEquipmentId(id);

        if (image != null && !image.isEmpty()) {
            try {

                if (currentEquipment.getImageUrl() != null && !currentEquipment.getImageUrl().isEmpty()) {
                    Map deleteImage = this.cloudinaryService.deleteFileByUrl(currentEquipment.getImageUrl());
                    System.out.println("Imagen anterior eliminada: " + deleteImage);
                }


                String imageUrl = this.cloudinaryService.uploadImage(image);
                equipmentToUpdate.setImageUrl(imageUrl);

            } catch (Exception e) {
                throw new RuntimeException("Error al procesar la imagen en Cloudinary", e);
            }
        } else {
            equipmentToUpdate.setImageUrl(currentEquipment.getImageUrl());
        }


        EquipmentModel equipmentModelSaved = this.equipmentUseCase.update(
                equipmentToUpdate,
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
                equipmentModelSaved.getSenaInventoryTag(),
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
                equipmentModelSaved.getUpdateAt(),
                equipmentModelSaved.getImageUrl(),
                equipmentModelSaved.getDescription() != null ? equipmentModelSaved.getDescription() : "No hay descripcion para est equipo"
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

    @GetMapping("/getFiles/{equipmentId}")
    public ResponseEntity<List<EquipmentMediaEntity>> getFiles(@PathVariable("equipmentId") Long id) {
        try {
            return new ResponseEntity<>(this.equipmentUseCase.getFiles(id), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<EquipmentResponseDto> getById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(
                this.equipmentMapper.toResponse(this.equipmentUseCase.getById(id)),
                HttpStatus.OK);
    }

    @GetMapping("/summaryStatics/card")
    public ResponseEntity<Map<String, Long>> getSummaryStatics() {
        return new ResponseEntity<>(this.equipmentUseCase.getSummaryStatics(), HttpStatus.OK);
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

    @GetMapping("/get-all-by-sena-inventory-tag/{code}")
    public ResponseEntity<List<EquipmentResponseDto>> getAllByISenaInventoryTag(@PathVariable("code") String code) {
        List<EquipmentModel> equipmentModelList = this.equipmentUseCase.getAllBySenaInventoryTag(code);
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
    public ResponseEntity<EquipmentResponseDto> changeState(@PathVariable("id") Long id, @PathVariable("state") String state) {
        System.out.println(state);
         EquipmentModel equipmentModelSaved = this.equipmentUseCase.changeState(id, state);
        EquipmentResponseDto response = new EquipmentResponseDto(
                equipmentModelSaved.getEquipmentId(),
                equipmentModelSaved.getInternalCode(),
                equipmentModelSaved.getEquipmentName(),
                equipmentModelSaved.getBrand(),
                equipmentModelSaved.getModel(),
                equipmentModelSaved.getSerialNumber(),
                equipmentModelSaved.getAcquisitionDate(),
                equipmentModelSaved.getMaintenanceDate(),
                equipmentModelSaved.getSenaInventoryTag(),
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
                equipmentModelSaved.getUpdateAt(),
                equipmentModelSaved.getImageUrl(),
                equipmentModelSaved.getDescription() != null ? equipmentModelSaved.getDescription() : "No hay descripcion para est equipo"
        );
        return new ResponseEntity<>( response ,HttpStatus.OK);
    }

    @PutMapping(value = "/change-image/{equipmentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> changeImage(@RequestPart("image") MultipartFile image, @PathVariable("equipmentId") Long equipmentId) {

        return new ResponseEntity<>(this.equipmentUseCase.changeImage(image, equipmentId), HttpStatus.OK);

    }

    @PostMapping(value = "/uploadFile/{equipmentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<EquipmentMediaEntity>> saveFiles(@RequestPart("files") List<MultipartFile> files, @PathVariable("equipmentId") Long equipmentId) {
        try {
            List<EquipmentMediaEntity> entities = this.equipmentUseCase.saveFiles(files, equipmentId);
            return new ResponseEntity<>(entities, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/deleteFile/{public_id}")
    public ResponseEntity<Boolean> deleteFile(@PathVariable("public_id") String public_id) {
        try {
            return new ResponseEntity<>(this.equipmentUseCase.deleteFile(public_id), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


}


