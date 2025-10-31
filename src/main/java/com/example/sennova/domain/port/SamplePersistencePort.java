package com.example.sennova.domain.port;

import com.example.sennova.domain.model.testRequest.SampleModel;

public interface SamplePersistencePort {
    
    SampleModel save(SampleModel sampleModel);
}
