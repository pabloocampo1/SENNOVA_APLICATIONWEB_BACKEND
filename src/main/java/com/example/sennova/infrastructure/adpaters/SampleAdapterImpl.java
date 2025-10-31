package com.example.sennova.infrastructure.adpaters;

import com.example.sennova.domain.model.testRequest.SampleModel;
import com.example.sennova.domain.port.SamplePersistencePort;
import com.example.sennova.infrastructure.mapperDbo.SampleMapperDbo;
import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.SampleEntity;
import com.example.sennova.infrastructure.persistence.repositoryJpa.SampleRepositoryJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SampleAdapterImpl implements SamplePersistencePort {

    private final SampleRepositoryJpa sampleRepositoryJpa;
    private final SampleMapperDbo sampleMapperDbo;

    @Autowired
    public SampleAdapterImpl(SampleRepositoryJpa sampleRepositoryJpa, SampleMapperDbo sampleMapperDbo) {
        this.sampleRepositoryJpa = sampleRepositoryJpa;
        this.sampleMapperDbo = sampleMapperDbo;
    }

    @Override
    public SampleModel save(SampleModel sampleModel) {
        SampleEntity sampleEntity = this.sampleRepositoryJpa.save(this.sampleMapperDbo.toEntity(sampleModel));
        return this.sampleMapperDbo.toModel(sampleEntity);
    }
}
