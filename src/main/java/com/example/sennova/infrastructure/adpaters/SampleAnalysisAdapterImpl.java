package com.example.sennova.infrastructure.adpaters;

import com.example.sennova.domain.model.testRequest.SampleAnalysisModel;
import com.example.sennova.domain.port.SampleAnalysisPersistencePort;
import com.example.sennova.infrastructure.mapperDbo.SampleAnalysisMapperDbo;
import com.example.sennova.infrastructure.mapperDbo.SampleMapperDbo;
import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.SampleAnalysisEntity;
import com.example.sennova.infrastructure.persistence.repositoryJpa.SampleAnalysisRepositoryJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SampleAnalysisAdapterImpl implements SampleAnalysisPersistencePort {

    private final SampleAnalysisRepositoryJpa sampleAnalysisRepositoryJpa;
    private final SampleAnalysisMapperDbo sampleAnalysisMapperDbo;

    @Autowired
    public SampleAnalysisAdapterImpl(SampleAnalysisRepositoryJpa sampleAnalysisRepositoryJpa, SampleAnalysisMapperDbo sampleAnalysisMapperDbo) {
        this.sampleAnalysisRepositoryJpa = sampleAnalysisRepositoryJpa;
        this.sampleAnalysisMapperDbo = sampleAnalysisMapperDbo;
    }


    @Override
    public SampleAnalysisModel save(SampleAnalysisModel sampleAnalysisModel) {
        SampleAnalysisEntity sampleAnalysisEntity = this.sampleAnalysisRepositoryJpa.save(this.sampleAnalysisMapperDbo.toEntity(sampleAnalysisModel));
        return this.sampleAnalysisMapperDbo.toModel(sampleAnalysisEntity);
    }
}
