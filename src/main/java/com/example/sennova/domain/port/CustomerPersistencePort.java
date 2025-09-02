package com.example.sennova.domain.port;

import com.example.sennova.infrastructure.persistence.entities.CustomerEntity;

import java.util.List;

public interface CustomerPersistencePort {

    List<CustomerEntity> getAll();
}
