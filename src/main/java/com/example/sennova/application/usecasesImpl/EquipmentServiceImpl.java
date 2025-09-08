package com.example.sennova.application.usecasesImpl;

import com.example.sennova.application.dto.UserDtos.UserResponse;
import com.example.sennova.application.mapper.UserMapper;
import com.example.sennova.application.usecases.EquipmentUseCase;
import com.example.sennova.application.usecases.LocationEquipmentUseCase;
import com.example.sennova.application.usecases.UsageEquipmentUseCase;
import com.example.sennova.application.usecases.UserUseCase;
import com.example.sennova.domain.model.EquipmentLocationModel;
import com.example.sennova.domain.model.EquipmentModel;
import com.example.sennova.domain.model.EquipmentUsageModel;
import com.example.sennova.domain.model.UserModel;
import com.example.sennova.domain.port.EquipmentPersistencePort;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentServiceImpl implements EquipmentUseCase {

    // variants for manage the state
    private static final String STATUS_ACTIVE = "Activo";
    private static final String STATUS_DECOMMISSIONED = "Dado de baja";
    private static final String STATUS_OUT_OF_SERVICE = "Fuera de servicio";

    private final UserMapper userMapper;
    private final EquipmentPersistencePort equipmentPersistencePort;
    private final LocationEquipmentUseCase locationEquipmentUseCase;
    private final UsageEquipmentUseCase usageEquipmentUseCase;
    private final UserUseCase userUseCase;

    @Autowired
    public EquipmentServiceImpl(@Lazy UserMapper userMapper, EquipmentPersistencePort equipmentPersistencePort, LocationEquipmentUseCase locationEquipmentUseCase, UsageEquipmentUseCase usageEquipmentUseCase, UserUseCase userUseCase) {
        this.userMapper = userMapper;
        this.equipmentPersistencePort = equipmentPersistencePort;
        this.locationEquipmentUseCase = locationEquipmentUseCase;
        this.usageEquipmentUseCase = usageEquipmentUseCase;
        this.userUseCase = userUseCase;
    }

    @Override
    @Transactional
    public EquipmentModel save(EquipmentModel equipmentModel, Long responsibleId, Long locationId, Long usageId) {

        // validate the state
        String state = equipmentModel.getState();
        if (!state.equals(STATUS_ACTIVE)
                && !state.equals(STATUS_DECOMMISSIONED)
                && !state.equals(STATUS_OUT_OF_SERVICE)) {
            throw new IllegalArgumentException("Estado de equipo inválido. Los valores permitidos son: Activo, Dado de baja, Fuera de servicio.");
        }

        // change the attribute available according the state
        equipmentModel.setAvailable(!state.equals(STATUS_DECOMMISSIONED) && !state.equals(STATUS_OUT_OF_SERVICE));

        // validate if the number serial is unique
        Long serialNumber = equipmentModel.getSerialNumber();
        if (this.equipmentPersistencePort.existsBySerialNumber(serialNumber)){
            throw new IllegalArgumentException("El numero serial ya existe. debe de ser unico");
        }

        String internalCode = equipmentModel.getInternalCode();
        if (this.equipmentPersistencePort.existsByInternalCode(internalCode)){
            throw new IllegalArgumentException("El codigo interno del equipo:" + equipmentModel.getEquipmentName() + "Debe de ser unico");
        }

        UserResponse user = this.userUseCase.findById(responsibleId);
        equipmentModel.setResponsible(this.userMapper.toModel(user));

        EquipmentLocationModel equipmentLocationModel = this.locationEquipmentUseCase.getById(locationId);
        equipmentModel.setLocation(equipmentLocationModel);

        EquipmentUsageModel equipmentUsageModel = this.usageEquipmentUseCase.getById(usageId);
        equipmentModel.setUsage(equipmentUsageModel);

        // validate the date
        return this.equipmentPersistencePort.save(equipmentModel);
    }


    @Override
    public EquipmentModel update(@Valid EquipmentModel equipmentModel, @Valid Long id) {
        return null;
    }

    @Override
    public EquipmentModel getById(Long id) {
        return null;
    }

    @Override
    public List<EquipmentModel> getAllById(String name) {
        return List.of();
    }

    @Override
    public Page<EquipmentModel> getAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<EquipmentModel> getAll() {
        return List.of();
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public List<EquipmentModel> getByLocation(Long locationId) {
        return List.of();
    }

    @Override
    public List<EquipmentModel> getByUsage(Long usageId) {
        return List.of();
    }

    @Override
    public void changeState(Long id, String state) {

    }
}
