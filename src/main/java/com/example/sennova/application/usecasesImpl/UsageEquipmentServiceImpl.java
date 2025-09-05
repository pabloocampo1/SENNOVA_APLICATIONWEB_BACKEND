package com.example.sennova.application.usecasesImpl;

import com.example.sennova.application.usecases.UsageEquipmentUseCase;
import com.example.sennova.domain.model.EquipmentUsageModel;
import com.example.sennova.domain.port.UsageEquipmentPersistencePort;
import com.example.sennova.web.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsageEquipmentServiceImpl implements UsageEquipmentUseCase {
    private final UsageEquipmentPersistencePort usageEquipmentPersistencePort;

    @Autowired
    public UsageEquipmentServiceImpl(UsageEquipmentPersistencePort usageEquipmentPersistencePort) {
        this.usageEquipmentPersistencePort = usageEquipmentPersistencePort;
    }

    @Override
    @Transactional
    public EquipmentUsageModel save(@Valid  EquipmentUsageModel equipmentUsageModel) {
        return this.usageEquipmentPersistencePort.save(equipmentUsageModel);
    }

    @Override
    @Transactional
    public EquipmentUsageModel update(Long id, EquipmentUsageModel equipmentUsageModel) {
        if(!this.usageEquipmentPersistencePort.existsById(id)){
            throw  new IllegalArgumentException("No se encontro el elemento con id: " + id);
        }

        if(equipmentUsageModel.getEquipmentUsageId() == null){
            throw  new IllegalArgumentException("No se pudo editar el elemento, hubo un error en el sistema.: " + id);
        }
        return this.usageEquipmentPersistencePort.update(equipmentUsageModel, id);
    }

    @Override
    public List<EquipmentUsageModel> getAllByName(@Valid  String name) {
        return this.usageEquipmentPersistencePort.findAllByName(name);
    }

    @Override
    public Page<EquipmentUsageModel> getAll(@Valid Pageable pageable) {
        return this.usageEquipmentPersistencePort.findAll(pageable);
    }

    @Override
    public List<EquipmentUsageModel> getAll() {
        return this.usageEquipmentPersistencePort.findAll();
    }

    @Override
    public EquipmentUsageModel getById(@Valid Long id) {
        return this.usageEquipmentPersistencePort.findById(id);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if(!this.usageEquipmentPersistencePort.existsById(id)){
            throw new ResourceNotFoundException("No se pudo realizar la accion porque no se encontro el usuario en el servidor, intentalo mas tarde.");
        }

        this.usageEquipmentPersistencePort.deleteById(id);
    }
}
