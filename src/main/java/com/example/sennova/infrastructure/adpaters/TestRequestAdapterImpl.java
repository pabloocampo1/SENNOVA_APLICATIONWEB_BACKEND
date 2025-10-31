package com.example.sennova.infrastructure.adpaters;

import com.example.sennova.domain.model.testRequest.TestRequestModel;
import com.example.sennova.domain.port.TestRequestPersistencePort;
import com.example.sennova.infrastructure.mapperDbo.TestRequestMapperDbo;
import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.TestRequestEntity;
import com.example.sennova.infrastructure.persistence.repositoryJpa.TestRequestRepositoryJpa;
import org.springframework.stereotype.Repository;

@Repository
public class TestRequestAdapterImpl implements TestRequestPersistencePort {

    private final TestRequestRepositoryJpa testRequestRepositoryJpa;
    private final TestRequestMapperDbo testRequestMapperDbo;

    public TestRequestAdapterImpl(TestRequestRepositoryJpa testRequestRepositoryJpa, TestRequestMapperDbo testRequestMapperDbo) {
        this.testRequestRepositoryJpa = testRequestRepositoryJpa;
        this.testRequestMapperDbo = testRequestMapperDbo;
    }

    @Override
    public TestRequestModel save(TestRequestModel testRequestModel) {
        TestRequestEntity testRequestEntity = this.testRequestRepositoryJpa.save(this.testRequestMapperDbo.toEntity(testRequestModel));
        return this.testRequestMapperDbo.toModel(testRequestEntity);
    }
}
