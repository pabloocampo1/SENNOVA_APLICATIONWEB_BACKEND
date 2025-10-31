package com.example.sennova.infrastructure.mapperDbo;

import com.example.sennova.domain.model.testRequest.TestRequestModel;
import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.TestRequestEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {SampleMapperDbo.class, CustomerMapperDbo.class})
public interface TestRequestMapperDbo {

    TestRequestModel toModel(TestRequestEntity testRequestEntity);
    TestRequestEntity toEntity(TestRequestModel testRequestModel);
}
