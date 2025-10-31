package com.example.sennova.application.usecasesImpl;

import com.example.sennova.application.dto.testeRequest.CustomerRequestRecord;
import com.example.sennova.application.dto.testeRequest.ProductQuantityQuote;
import com.example.sennova.application.dto.testeRequest.SampleRequestRecord;
import com.example.sennova.application.dto.testeRequest.TestRequestRecord;
import com.example.sennova.application.mapper.CustomerMapper;
import com.example.sennova.application.usecases.*;
import com.example.sennova.domain.model.ProductModel;
import com.example.sennova.domain.model.testRequest.CustomerModel;
import com.example.sennova.domain.model.testRequest.SampleAnalysisModel;
import com.example.sennova.domain.model.testRequest.SampleModel;
import com.example.sennova.domain.model.testRequest.TestRequestModel;
import com.example.sennova.domain.port.TestRequestPersistencePort;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class TestRequestServiceImpl implements TestRequestUseCase {

    private final CustomerMapper customerMapper;
    private final CustomerUseCase customerUseCase;
    private final SampleUseCase sampleUseCase;
    private final SampleAnalysisUseCase sampleAnalysisUseCase;
    private final ProductUseCase productUseCase;
    private final TestRequestPersistencePort testRequestPersistencePort;

    @Autowired
    public TestRequestServiceImpl(CustomerMapper customerMapper, CustomerUseCase customerUseCase, SampleUseCase sampleUseCase, SampleAnalysisUseCase sampleAnalysisUseCase, ProductUseCase productUseCase, TestRequestPersistencePort testRequestPersistencePort) {
        this.customerMapper = customerMapper;
        this.customerUseCase = customerUseCase;
        this.sampleUseCase = sampleUseCase;
        this.sampleAnalysisUseCase = sampleAnalysisUseCase;
        this.productUseCase = productUseCase;
        this.testRequestPersistencePort = testRequestPersistencePort;
    }


    @Override
    public List<TestRequestModel> getAllTestRequest() {
        return List.of();
    }

    @Override
    @Transactional
    public TestRequestModel save(TestRequestRecord testRequestRecord) {

        TestRequestModel testRequestModel = new TestRequestModel();
        testRequestModel.setIsApproved(false);


        // create the customer.
        // search the customer, if exist don't save and search and get. if not exist save
        CustomerRequestRecord customerRequestRecord = testRequestRecord.customer();
        CustomerModel customer = this.customerUseCase.save(this.customerMapper.toModel(customerRequestRecord));

        testRequestModel.setCustomer(customer);

        int currentYear = LocalDate.now().getYear();
        int randomNum = (int) (Math.random() * 9000) + 1000;
        String code = currentYear + " - " + randomNum ;

        testRequestModel.setRequestCode(code);


        // create the list of the sample with products.
        // calculte the final price

        AtomicReference<Double> finalPrice = new AtomicReference<>((double) 0);
        AtomicInteger countSampleCode = new AtomicInteger(1)  ;

        TestRequestModel testRequestSaved =  this.testRequestPersistencePort.save(testRequestModel);

        List<SampleModel> samples = new ArrayList<>();
        List<SampleRequestRecord> sampleRequestRecords = testRequestRecord.samples();
        sampleRequestRecords.forEach(sampleRequestRecord -> {
                SampleModel sampleModel = new SampleModel();
                 sampleModel.setMatrix(sampleRequestRecord.matrix());
                 sampleModel.setDescription(sampleRequestRecord.description());
                 sampleModel.setSampleCode("M"+countSampleCode.getAndIncrement() + " - " + randomNum);
                 sampleModel.setTestRequest(testRequestSaved);

                 // save the sample, after create the entity with the products(analysis to do) and sample to save results about that analysis
                SampleModel sampleSaved = this.sampleUseCase.save(sampleModel);



                 // create the entity to result
                 List<ProductQuantityQuote> analysisAndQuantity = sampleRequestRecord.products();
                 analysisAndQuantity.forEach(analysis -> {
                     SampleAnalysisModel sampleAnalysisModel = new SampleAnalysisModel();
                     ProductModel product = this.productUseCase.getById(analysis.getProductId());
                     sampleAnalysisModel.setProduct(product);
                     sampleAnalysisModel.setSample(sampleSaved);

                             // save the entity to register result
                     this.sampleAnalysisUseCase.save(sampleAnalysisModel);

                                Integer quantity = analysis.getQuantity();
                                double priceUniteAnalysis = product.getPrice();

                                finalPrice.updateAndGet(v -> v + priceUniteAnalysis * quantity);


                 });

        });



        // add the final price to the testRequest
        testRequestSaved.setPrice(finalPrice.get());

        // save the test request to get the id to generate the code request

        // create the notification.
        return this.testRequestPersistencePort.save(testRequestSaved);
    }

    @Override
    public TestRequestModel update(TestRequestModel testRequestModel) {
        return null;
    }

    @Override
    public List<TestRequestModel> getAllTestRequestNoCheck() {
        return List.of();
    }

    @Override
    public List<TestRequestModel> getTestRequestByCode() {
        return List.of();
    }
}
