package com.example.sennova.application.usecasesImpl;


import com.example.sennova.application.usecases.SampleAnalysisUseCase;
import com.example.sennova.domain.model.testRequest.SampleAnalysisModel;
import com.example.sennova.domain.port.SampleAnalysisPersistencePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SampleAnalysisUseCaseServiceImpl implements SampleAnalysisUseCase {

    private final SampleAnalysisPersistencePort sampleAnalysisPersistencePort;

    @Autowired
    public SampleAnalysisUseCaseServiceImpl(SampleAnalysisPersistencePort sampleAnalysisPersistencePort) {
        this.sampleAnalysisPersistencePort = sampleAnalysisPersistencePort;
    }

    @Override
    public SampleAnalysisModel save(SampleAnalysisModel sampleAnalysisModel) {

        return this.sampleAnalysisPersistencePort.save(sampleAnalysisModel);
    }
}
