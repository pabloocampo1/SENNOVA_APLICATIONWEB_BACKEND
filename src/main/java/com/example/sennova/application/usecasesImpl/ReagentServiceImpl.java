package com.example.sennova.application.usecasesImpl;

import com.example.sennova.application.dto.UserDtos.UserResponse;
import com.example.sennova.application.mapper.UserMapper;
import com.example.sennova.application.usecases.LocationUseCase;
import com.example.sennova.application.usecases.ReagentUseCase;
import com.example.sennova.application.usecases.UsageUseCase;
import com.example.sennova.application.usecases.UserUseCase;
import com.example.sennova.domain.constants.UnitsOfMeasureEnum;
import com.example.sennova.domain.model.LocationModel;
import com.example.sennova.domain.model.ReagentModel;
import com.example.sennova.domain.model.UsageModel;
import com.example.sennova.domain.model.UserModel;
import com.example.sennova.domain.port.ReagentPersistencePort;
import com.example.sennova.infrastructure.persistence.entities.inventoryReagentsEntities.ReagentsEntity;
import com.example.sennova.infrastructure.restTemplate.CloudinaryService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class ReagentServiceImpl implements ReagentUseCase {

    private final ReagentPersistencePort reagentPersistencePort;
    private final CloudinaryService cloudinaryService;
    private final LocationUseCase locationUseCase;
    private final UserUseCase userUseCase;
    private final UserMapper userMapper;
    private final UsageUseCase usageUseCase;

    @Autowired
    public ReagentServiceImpl(ReagentPersistencePort reagentPersistencePort, CloudinaryService cloudinaryService, LocationUseCase locationUseCase, UserUseCase userUseCase, UserMapper userMapper, UsageUseCase usageUseCase) {
        this.reagentPersistencePort = reagentPersistencePort;
        this.cloudinaryService = cloudinaryService;
        this.locationUseCase = locationUseCase;
        this.userUseCase = userUseCase;
        this.userMapper = userMapper;
        this.usageUseCase = usageUseCase;
    }


    @Override
    public ReagentModel save(ReagentModel reagentModel, MultipartFile multipartFile, Long userId, Long responsibleId, Long locationId, Long usageId) {

        // add  the user responsible
        UserResponse user = this.userUseCase.findById(responsibleId);
        reagentModel.setUserModel(this.userMapper.toModel(user));

        // add the location
        LocationModel locationModel = this.locationUseCase.getById(locationId);
        reagentModel.setLocation(locationModel);

        // add the usage
        UsageModel usageModel = this.usageUseCase.getById(usageId);
        reagentModel.setUsage(usageModel);


        // validate units
        if (reagentModel.getUnits() < 1)
            throw new IllegalArgumentException("La cantidad del reactivo no puede ser menor a 1.");
        if (reagentModel.getQuantity() < 1)
            throw new IllegalArgumentException("La cantidad del reactivo no puede ser menor a 1.");

        boolean isValidTheMeasurementUnit = Arrays.stream(UnitsOfMeasureEnum.values())
                .anyMatch(unit -> unit.name().equalsIgnoreCase(reagentModel.getMeasurementUnit()));

        if (!isValidTheMeasurementUnit) {
            throw new IllegalArgumentException("No se aceptan unidades de medida diferentes a las del sistema.");
        }


        try {
            if (multipartFile != null && !multipartFile.isEmpty()) {
                String imageSaved = cloudinaryService.uploadImage(multipartFile);
                reagentModel.setImageUrl(imageSaved);
            }

            // the user id is for make one notification to say who save one reagent

            return reagentPersistencePort.save(reagentModel);

        } catch (Exception e) {
            if (reagentModel.getImageUrl() != null) {
                try {
                    cloudinaryService.deleteFileByUrl(reagentModel.getImageUrl());
                } catch (Exception ex) {
                    System.out.println("Error deleting image after failure: {}" + ex.getMessage());
                }
            }
            throw new RuntimeException("Error al guardar el reactivo: " + e.getMessage(), e);
        }



    }

    @Override
    @Transactional
    public ReagentModel update(ReagentModel reagentModel, Long reagentId, MultipartFile multipartFile) {
        return null;
    }

    @Override
    public ReagentModel getById( @Valid Long id) {
        return this.reagentPersistencePort.findById(id);
    }

    @Override
    public ReagentsEntity getEntity(Long id) {
        return this.reagentPersistencePort.findEntityById(id);
    }

    @Override
    public List<ReagentModel> getAll() {
        return this.reagentPersistencePort.findAll();
    }

    @Override
    public Page<ReagentModel> getAll(Pageable pageable) {
        return this.reagentPersistencePort.findAll(pageable);
    }

    @Override
    public List<ReagentModel> getAllByName(@Valid  String name) {
        return this.reagentPersistencePort.findAllByName(name);
    }

    @Override
    public List<ReagentModel> getAllByLocation(@Valid Long locationId) {
        LocationModel locationModel = this.locationUseCase.getById(locationId);
        return this.reagentPersistencePort.findAllByLocation(locationModel);
    }

    @Override
    @Transactional
    public void deleteById(@Valid  Long id) {

        // added logic for delete files and more
        this.reagentPersistencePort.deleteById(id);
    }
}
