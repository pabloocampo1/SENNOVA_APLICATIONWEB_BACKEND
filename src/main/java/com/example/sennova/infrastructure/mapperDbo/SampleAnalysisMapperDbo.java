package com.example.sennova.infrastructure.mapperDbo;

import com.example.sennova.domain.model.testRequest.SampleAnalysisModel;
import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.SampleAnalysisEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ProductMapperDbo.class, SampleMapperDbo.class})
public interface SampleAnalysisMapperDbo {
    SampleAnalysisEntity toEntity(SampleAnalysisModel sampleAnalysisModel);
    SampleAnalysisModel toModel(SampleAnalysisEntity sampleAnalysisEntity);
}
