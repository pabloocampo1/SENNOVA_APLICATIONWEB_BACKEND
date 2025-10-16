package com.example.sennova.application.usecasesImpl;

import com.example.sennova.application.dto.UserDtos.UserResponse;
import com.example.sennova.application.dto.inventory.ReagentInventory.UsageReagentRequest;
import com.example.sennova.application.mapper.UserMapper;
import com.example.sennova.application.usecases.LocationUseCase;
import com.example.sennova.application.usecases.ReagentUseCase;
import com.example.sennova.application.usecases.UsageUseCase;
import com.example.sennova.application.usecases.UserUseCase;
import com.example.sennova.domain.constants.ReagentStateCons;
import com.example.sennova.domain.constants.UnitsOfMeasureEnum;
import com.example.sennova.domain.model.LocationModel;
import com.example.sennova.domain.model.ReagentModel;
import com.example.sennova.domain.model.UsageModel;
import com.example.sennova.domain.model.UserModel;
import com.example.sennova.domain.port.ReagentPersistencePort;
import com.example.sennova.infrastructure.persistence.entities.inventoryReagentsEntities.ReagentMediaFilesEntity;
import com.example.sennova.infrastructure.persistence.entities.inventoryReagentsEntities.ReagentsEntity;
import com.example.sennova.infrastructure.persistence.entities.inventoryReagentsEntities.ReagentsUsageRecords;
import com.example.sennova.infrastructure.persistence.repositoryJpa.ReagentMediaFileRepository;
import com.example.sennova.infrastructure.persistence.repositoryJpa.UsageReagentRepositoryJpa;
import com.example.sennova.infrastructure.restTemplate.CloudinaryService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
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
    private final ReagentMediaFileRepository reagentMediaFileRepository;
    private final UsageUseCase usageUseCase;
    private final UsageReagentRepositoryJpa usageReagentRepositoryJpa;

    @Autowired
    public ReagentServiceImpl(ReagentPersistencePort reagentPersistencePort, CloudinaryService cloudinaryService, LocationUseCase locationUseCase, UserUseCase userUseCase, UserMapper userMapper, ReagentMediaFileRepository reagentMediaFileRepository, UsageUseCase usageUseCase, UsageReagentRepositoryJpa usageReagentRepositoryJpa) {
        this.reagentPersistencePort = reagentPersistencePort;
        this.cloudinaryService = cloudinaryService;
        this.locationUseCase = locationUseCase;
        this.userUseCase = userUseCase;
        this.userMapper = userMapper;
        this.reagentMediaFileRepository = reagentMediaFileRepository;
        this.usageUseCase = usageUseCase;
        this.usageReagentRepositoryJpa = usageReagentRepositoryJpa;
    }


    @Override
    public ReagentModel save(
            @Valid ReagentModel reagentModel,
            MultipartFile multipartFile,
            @Valid String performedBy,
            @Valid Long responsibleId,
            @Valid Long locationId,
            @Valid Long usageId) {


        // add  the user responsible
        UserResponse user = this.userUseCase.findById(responsibleId);
        reagentModel.setUser(this.userMapper.toModel(user));

        // add the location
        LocationModel locationModel = this.locationUseCase.getById(locationId);
        reagentModel.setLocation(locationModel);

        // add the usage
        UsageModel usageModel = this.usageUseCase.getById(usageId);
        reagentModel.setUsage(usageModel);


        // validate units
        // wait if this part are valid for the logic
        /*
        if (reagentModel.getUnits() < 1)
            throw new IllegalArgumentException("La cantidad del reactivo no puede ser menor a 1.");
        if (reagentModel.getQuantity() < 1)
            throw new IllegalArgumentException("La cantidad del reactivo no puede ser menor a 1.");

         */

        // see the expiration date for change the state
        LocalDate currentDate = LocalDate.now();
        LocalDate expirationDate = reagentModel.getExpirationDate();

        if (expirationDate.isBefore(currentDate)) {
            reagentModel.setStateExpiration(ReagentStateCons.STATE_EXPIRED);
        } else if (expirationDate.isAfter(currentDate)) {
            reagentModel.setStateExpiration(ReagentStateCons.STATE_NOT_EXPIRED);
        } else {
            reagentModel.setStateExpiration(ReagentStateCons.STATE_NOT_EXPIRED);
        }

        // validate the state of the quantity
        if (reagentModel.getQuantity() >= 1) {
            reagentModel.setState(ReagentStateCons.WITH_STOCK);
        } else {
            reagentModel.setState(ReagentStateCons.LOW_STOCK);
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
    public ReagentModel getById(@Valid Long id) {
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
    public List<ReagentModel> getAllByName(@Valid String name) {
        return this.reagentPersistencePort.findAllByName(name);
    }

    @Override
    public List<ReagentModel> getAllByLocation(@Valid Long locationId) {
        LocationModel locationModel = this.locationUseCase.getById(locationId);
        return this.reagentPersistencePort.findAllByLocation(locationModel);
    }

    @Override
    @Transactional
    public void deleteById(@Valid Long id) {

        // added logic for delete files and more
        this.reagentPersistencePort.deleteById(id);
    }

    @Override
    @Transactional
    public boolean deleteFile(String publicId) {
        System.out.println("llego esto bro: " + publicId);
        ReagentMediaFilesEntity file = this.reagentMediaFileRepository.findByPublicId(publicId);
        if (file != null) {
            file.setReagentEntity(null);
            reagentMediaFileRepository.deleteById(file.getReagentFileId());
            cloudinaryService.deleteFile(publicId);
        }

        return true;
    }

    @Override
    public List<ReagentMediaFilesEntity> getFiles(@Valid Long reagentId) {
        ReagentModel reagentModel = this.reagentPersistencePort.findById(reagentId);
        return this.reagentMediaFileRepository.findAllByReagentEntity_ReagentsId(reagentModel.getReagentsId());
    }

    @Override
    @Transactional
    public List<ReagentMediaFilesEntity> uploadFiles(@Valid Long reagentId, List<MultipartFile> multipartFiles) {

        // check if the reagent exists
        ReagentsEntity reagentsEntity = this.reagentPersistencePort.findEntityById(reagentId);

        List<ReagentMediaFilesEntity> reagentMediaFilesEntities = multipartFiles
                .stream()
                .map(file -> {

                    // save the file
                    Map<String, String> fileUpload = this.cloudinaryService.uploadFile(file);

                    // create the entity media for save
                    ReagentMediaFilesEntity reagentMediaFilesEntity = new ReagentMediaFilesEntity();
                    reagentMediaFilesEntity.setNameFile(fileUpload.get("originalFilename"));
                    reagentMediaFilesEntity.setReagentEntity(reagentsEntity);
                    reagentMediaFilesEntity.setType(fileUpload.get("contentType"));
                    reagentMediaFilesEntity.setUrl(fileUpload.get("secure_url"));
                    reagentMediaFilesEntity.setPublicId(fileUpload.get("public_id"));


                    return this.reagentMediaFileRepository.save(reagentMediaFilesEntity);

                })
                .toList();

        // cretae logic
        return reagentMediaFilesEntities;
    }

    @Override
    public ReagentModel changeQuantity(Long reagentId, Integer quantity) {
        // find the reagent
        ReagentModel reagent = this.reagentPersistencePort.findById(reagentId);


        // change the state
        reagent.setQuantity(quantity);

        if (quantity >= 1) {
            reagent.setState(ReagentStateCons.WITH_STOCK);
        } else {
            reagent.setState(ReagentStateCons.LOW_STOCK);
        }

        return this.reagentPersistencePort.save(reagent);
    }

    @Override
    public ReagentModel changeState(Long reagentId, String state) {
        // find the reagent
        ReagentModel reagent = this.reagentPersistencePort.findById(reagentId);

        switch (state) {
            case "SIN CONTENIDO":
                reagent.setState(ReagentStateCons.LOW_STOCK);
                reagent.setQuantity(0);
                ;

            case "CON CONTENIDO":
                reagent.setState(ReagentStateCons.WITH_STOCK);
                ;
            default:
                reagent.setState(ReagentStateCons.NO_VALUE);

        }

        return this.reagentPersistencePort.save(reagent);
    }

    @Override
    @Transactional
    public ReagentsUsageRecords saveUsage(UsageReagentRequest usageReagentRequest) {
        // get the reagent
        ReagentModel reagentModel = this.reagentPersistencePort.findById(usageReagentRequest.reagentId());

        // validate the stock
        if (usageReagentRequest.quantity() > reagentModel.getQuantity()) {
            throw new IllegalArgumentException("Cantidad inválida: la cantidad solicitada excede el número de unidades disponibles en inventario.");
        }

        // update the state and stock
        int newQuantity = reagentModel.getQuantity() - usageReagentRequest.quantity();
        reagentModel.setQuantity(newQuantity);

        if (newQuantity >= 1) {
            reagentModel.setState(ReagentStateCons.WITH_STOCK);
        } else {
            reagentModel.setState(ReagentStateCons.LOW_STOCK);
        }

        // save the reagent again.
        ReagentModel reagentUpdate = this.reagentPersistencePort.save(reagentModel);

        ReagentsUsageRecords reagentsUsageRecord = new ReagentsUsageRecords();
        reagentsUsageRecord.setNotes(usageReagentRequest.notes());
        reagentsUsageRecord.setUsedBy(usageReagentRequest.responsibleName());
        reagentsUsageRecord.setQuantity_used(usageReagentRequest.quantity());
        reagentsUsageRecord.setReagent(
                this.reagentPersistencePort.findEntityById(reagentUpdate.getReagentsId())
        );


        // save the record and return that data
        return this.usageReagentRepositoryJpa.save(reagentsUsageRecord);
    }

    @Override
    public List<ReagentsUsageRecords> getUsagesByReagentId(@Valid Long reagentId) {
        return this.usageReagentRepositoryJpa.findAllByReagent_ReagentsId(reagentId);
    }
}
