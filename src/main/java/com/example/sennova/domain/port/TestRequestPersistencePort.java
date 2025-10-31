package com.example.sennova.domain.port;

import com.example.sennova.application.usecases.TestRequestUseCase;
import com.example.sennova.domain.model.testRequest.TestRequestModel;

public interface TestRequestPersistencePort {
    TestRequestModel save(TestRequestModel testRequestModel);
}
