package com.example.sennova.application.usecasesImpl;

import com.example.sennova.application.usecases.LocationUseCase;
import com.example.sennova.domain.model.LocationModel;
import com.example.sennova.domain.port.LocationPersistencePort;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationUseCase {

    private final LocationPersistencePort locationPersistencePort;

    @Autowired
    public LocationServiceImpl(LocationPersistencePort locationPersistencePort) {
        this.locationPersistencePort = locationPersistencePort;
    }

    @Override
    @Transactional
    public LocationModel save(@Valid LocationModel locationModel) {
        return this.locationPersistencePort.save(locationModel);
    }

    @Override
    @Transactional
    public LocationModel update(@Valid Long id, @Valid LocationModel locationModel) {
        if(!this.locationPersistencePort.existById(id)){
            throw new UsernameNotFoundException("No se pudo encontrar la ubicacion con id : " + id);
        }
        if(locationModel.getEquipmentLocationId() == null){
            throw new IllegalArgumentException("Ah ocurrido un error al intentar editar la ubicacion, por favor intentalo mas tarde, o informa sobre el error. : " + id);
        }

        return this.locationPersistencePort.update(locationModel, id);
    }

    @Override
    public List<LocationModel> getAllByName(@Valid String name) {
        return this.locationPersistencePort.findAllByName(name);
    }

    @Override
    public LocationModel getById(@Valid Long id) {
        if(!this.locationPersistencePort.existById(id)){
            throw new IllegalArgumentException("No se pudo encontrar la ubicacion con id : " + id);
        }
        return this.locationPersistencePort.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        if(!this.locationPersistencePort.existById(id)){
            throw new IllegalArgumentException("No se pudo realizar la accion, intentalo de nuevo");
        }
        this.locationPersistencePort.deleteById(id);
    }

    @Override
    public Page<LocationModel> getAllPage(Pageable pageable) {
        return this.locationPersistencePort.findAllPage(pageable);
    }

    @Override
    public List<LocationModel> getAll() {
        return this.locationPersistencePort.findAll();
    }
}
