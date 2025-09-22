package com.example.sennova.application.usecasesImpl;

import com.example.sennova.application.dto.UserDtos.UserResponse;
import com.example.sennova.application.mapper.UserMapper;
import com.example.sennova.application.usecases.EquipmentUseCase;
import com.example.sennova.application.usecases.LocationEquipmentUseCase;
import com.example.sennova.application.usecases.UsageEquipmentUseCase;
import com.example.sennova.application.usecases.UserUseCase;
import com.example.sennova.domain.constants.EquipmentConstants;
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

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EquipmentServiceImpl implements EquipmentUseCase {

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
        if (!state.equals(EquipmentConstants.STATUS_ACTIVE)
                && !state.equals(EquipmentConstants.STATUS_DECOMMISSIONED)
                && !state.equals(EquipmentConstants.STATUS_OUT_OF_SERVICE)) {
            throw new IllegalArgumentException("Estado de equipo inválido. Los valores permitidos son: Activo, Dado de baja, Fuera de servicio.");
        }

        // change the attribute available according the state
        equipmentModel.setAvailable(!state.equals(EquipmentConstants.STATUS_DECOMMISSIONED) && !state.equals(EquipmentConstants.STATUS_OUT_OF_SERVICE));

        // validate if the number serial is unique
        Long serialNumber = equipmentModel.getSerialNumber();
        if (this.equipmentPersistencePort.existsBySerialNumber(serialNumber)) {
            throw new IllegalArgumentException("El numero serial ya existe. debe de ser unico");
        }

        String internalCode = equipmentModel.getInternalCode();
        if (this.equipmentPersistencePort.existsByInternalCode(internalCode)) {
            throw new IllegalArgumentException("El codigo interno del equipo: " + equipmentModel.getEquipmentName() + " Debe de ser unico");
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
    @Transactional
    public EquipmentModel update(@Valid EquipmentModel equipmentModel, @Valid Long id, Long responsibleId, Long locationId, Long usageId) {

        if (equipmentModel.getEquipmentId() == null) {
            throw new IllegalArgumentException("Error en el servidor al momento de editar el equipo. Por favor intentalo mas tarde o comunica el error");
        }

        if (!equipmentModel.getEquipmentId().equals(id)) {
            throw new IllegalArgumentException("Ocurrio un error en el sistema al intentar editar el elemento: los indentificadores no coinciden");
        }

        if (!this.equipmentPersistencePort.existById(equipmentModel.getEquipmentId())) {
            throw new IllegalArgumentException("EL equipo con id: " + equipmentModel.getEquipmentId() + " no existe en la base de datos");
        }

        String state = equipmentModel.getState();
        if (!state.equals(EquipmentConstants.STATUS_ACTIVE)
                && !state.equals(EquipmentConstants.STATUS_DECOMMISSIONED)
                && !state.equals(EquipmentConstants.STATUS_OUT_OF_SERVICE)) {
            throw new IllegalArgumentException("Estado de equipo inválido. Los valores permitidos son: Activo, Dado de baja, Fuera de servicio.");
        }

        // change the attribute available according the state
        equipmentModel.setAvailable(!state.equals(EquipmentConstants.STATUS_DECOMMISSIONED) && !state.equals(EquipmentConstants.STATUS_OUT_OF_SERVICE));

        // obtener la entidad para editar sin cambios para comparar valores y deterkinar la logica

        EquipmentModel equipmentToEdit = this.equipmentPersistencePort.findById(id);
       equipmentModel.setCreateAt(equipmentToEdit.getCreateAt());
       equipmentToEdit.setUpdateAt(LocalDateTime.now());

        // si el numero serial es diferente, entonces buscamos que no exista en la db
        if (!equipmentModel.getSerialNumber().equals(equipmentToEdit.getSerialNumber())) {
            if (this.equipmentPersistencePort.existsBySerialNumber(equipmentModel.getSerialNumber())) {
                throw new IllegalArgumentException("El numero serial ya existe. debe de ser unico");
            }
        }

        // si el codigo interno es diferente, entonces buscamos que no exista en la db
        if (!equipmentModel.getInternalCode().equals(equipmentToEdit.getInternalCode())) {
            if (this.equipmentPersistencePort.existsByInternalCode(equipmentModel.getInternalCode())) {
                throw new IllegalArgumentException("El codigo interno del equipo: " + equipmentModel.getEquipmentName() + " Debe de ser unico");
            }
        }

        UserResponse user = this.userUseCase.findById(responsibleId);
        equipmentModel.setResponsible(this.userMapper.toModel(user));

        EquipmentLocationModel equipmentLocationModel = this.locationEquipmentUseCase.getById(locationId);
        equipmentModel.setLocation(equipmentLocationModel);

        EquipmentUsageModel equipmentUsageModel = this.usageEquipmentUseCase.getById(usageId);
        equipmentModel.setUsage(equipmentUsageModel);

        return this.equipmentPersistencePort.update(equipmentModel);
    }

    @Override
    public EquipmentModel getById(Long id) {
        return this.equipmentPersistencePort.findById(id);
    }

    @Override
    public List<EquipmentModel> getAllByName(String name) {
        return this.equipmentPersistencePort.findAllByName(name);
    }

    @Override
    public List<EquipmentModel> getAllByInternalCode(String internalCode) {

        return this.equipmentPersistencePort.findAllByInternalCode(internalCode);
    }

    @Override
    public Page<EquipmentModel> getAll(Pageable pageable) {
        return this.equipmentPersistencePort.getAllPage(pageable);
    }

    @Override
    public List<EquipmentModel> getAll() {
        return this.equipmentPersistencePort.findAll();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!this.equipmentPersistencePort.existById(id)) {
            throw new IllegalArgumentException("El equipo que deseas eliminar no existe.");
        }

        this.equipmentPersistencePort.delete(id);
    }

    @Override
    public List<EquipmentModel> getByLocation(Long locationId) {
        EquipmentLocationModel equipmentLocationModel = this.locationEquipmentUseCase.getById(locationId);
        return this.equipmentPersistencePort.findAllByLocation(equipmentLocationModel);
    }

    @Override
    public List<EquipmentModel> getByUsage(Long usageId) {
        EquipmentUsageModel equipmentUsageModel = this.usageEquipmentUseCase.getById(usageId);
        return this.equipmentPersistencePort.findAllByUsage(equipmentUsageModel);
    }

    @Override
    @Transactional
    public void changeState(Long id, String state) {
        if (id == null || state == null) {
            throw new IllegalArgumentException("Ocurrio un error en el sistema, por favor intentalo mas tarde.");
        }
        this.equipmentPersistencePort.changeState(id, state);

    }
}
