package com.example.sennova.infrastructure.adpaters;


import com.example.sennova.domain.port.CustomerPersistencePort;
import com.example.sennova.infrastructure.persistence.entities.CustomerEntity;
import com.example.sennova.infrastructure.persistence.repositoryJpa.CustomersRepositoryJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomersAdapterImpl implements CustomerPersistencePort {
    @Autowired
    private CustomersRepositoryJpa customersRepositoryJpa;

    public List<CustomerEntity> getAll(){
        return this.customersRepositoryJpa.findAll();
    }
}
