package com.example.sennova.application.usecasesImpl;

import com.example.sennova.application.usecases.LocationEquipmentUseCase;
import com.example.sennova.domain.model.EquipmentLocationModel;
import com.example.sennova.domain.port.LocationEquipmentPersistencePort;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationEquipmentServiceImpl implements LocationEquipmentUseCase {

    private final LocationEquipmentPersistencePort locationEquipmentPersistencePort;

    @Autowired
    public LocationEquipmentServiceImpl(LocationEquipmentPersistencePort locationEquipmentPersistencePort) {
        this.locationEquipmentPersistencePort = locationEquipmentPersistencePort;
    }

    @Override
    @Transactional
    public EquipmentLocationModel save(@Valid EquipmentLocationModel equipmentLocationModel) {
        return this.locationEquipmentPersistencePort.save(equipmentLocationModel);
    }

    @Override
    @Transactional
    public EquipmentLocationModel update(@Valid Long id, @Valid EquipmentLocationModel equipmentLocationModel) {
        if(!this.locationEquipmentPersistencePort.existById(id)){
            throw new UsernameNotFoundException("No se pudo encontrar la ubicacion con id : " + id);
        }
        if(equipmentLocationModel.getEquipmentLocationId() == null){
            throw new IllegalArgumentException("Ah ocurrido un error al intentar editar la ubicacion, por favor intentalo mas tarde, o informa sobre el error. : " + id);
        }

        return this.locationEquipmentPersistencePort.update(equipmentLocationModel, id);
    }

    @Override
    public List<EquipmentLocationModel> getAllByName(@Valid String name) {
        return this.locationEquipmentPersistencePort.findAllByName(name);
    }

    @Override
    public EquipmentLocationModel getById(@Valid Long id) {
        if(!this.locationEquipmentPersistencePort.existById(id)){
            throw new UsernameNotFoundException("No se pudo encontrar la ubicacion con id : " + id);
        }
        return this.locationEquipmentPersistencePort.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        if(!this.locationEquipmentPersistencePort.existById(id)){
            throw new IllegalArgumentException("No se pudo realizar la accion, intentalo de nuevo");
        }
        this.locationEquipmentPersistencePort.deleteById(id);
    }

    @Override
    public Page<EquipmentLocationModel> getAllPage(Pageable pageable) {
        return this.locationEquipmentPersistencePort.findAllPage(pageable);
    }
}
