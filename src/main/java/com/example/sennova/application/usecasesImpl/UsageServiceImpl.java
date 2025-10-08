package com.example.sennova.application.usecasesImpl;

import com.example.sennova.application.usecases.UsageUseCase;
import com.example.sennova.domain.model.UsageModel;
import com.example.sennova.domain.port.UsagePersistencePort;
import com.example.sennova.web.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsageServiceImpl implements UsageUseCase {
    private final UsagePersistencePort usagePersistencePort;

    @Autowired
    public UsageServiceImpl(UsagePersistencePort usagePersistencePort) {
        this.usagePersistencePort = usagePersistencePort;
    }

    @Override
    @Transactional
    public UsageModel save(@Valid UsageModel usageModel) {
        return this.usagePersistencePort.save(usageModel);
    }

    @Override
    @Transactional
    public UsageModel update(Long id, UsageModel usageModel) {
        if(!this.usagePersistencePort.existsById(id)){
            throw  new IllegalArgumentException("No se encontro el elemento con id: " + id);
        }

        if(usageModel.getEquipmentUsageId() == null){
            throw  new IllegalArgumentException("No se pudo editar el elemento, hubo un error en el sistema.: " + id);
        }
        return this.usagePersistencePort.update(usageModel, id);
    }

    @Override
    public List<UsageModel> getAllByName(@Valid  String name) {
        return this.usagePersistencePort.findAllByName(name);
    }

    @Override
    public Page<UsageModel> getAll(@Valid Pageable pageable) {
        return this.usagePersistencePort.findAll(pageable);
    }

    @Override
    public List<UsageModel> getAll() {
        return this.usagePersistencePort.findAll();
    }

    @Override
    public UsageModel getById(@Valid Long id) {
        return this.usagePersistencePort.findById(id);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if(!this.usagePersistencePort.existsById(id)){
            throw new ResourceNotFoundException("No se pudo realizar la accion porque no se encontro el usuario en el servidor, intentalo mas tarde.");
        }

        this.usagePersistencePort.deleteById(id);
    }
}
