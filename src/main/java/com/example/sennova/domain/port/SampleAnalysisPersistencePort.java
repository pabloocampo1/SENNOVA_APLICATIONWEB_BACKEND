package com.example.sennova.domain.port;

import com.example.sennova.domain.model.testRequest.SampleAnalysisModel;

public interface SampleAnalysisPersistencePort {

    SampleAnalysisModel save(SampleAnalysisModel sampleAnalysisModel);
}
