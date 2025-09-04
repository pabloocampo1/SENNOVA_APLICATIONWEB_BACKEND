package com.example.sennova.application.usecasesImpl;

import com.example.sennova.application.dto.productDtos.ProductRequestDto;
import com.example.sennova.application.dto.productDtos.ProductResponseBasicDto;
import com.example.sennova.application.mapper.ProductMapper;
import com.example.sennova.application.usecases.ProductUseCase;
import com.example.sennova.domain.model.ProductModel;
import com.example.sennova.domain.port.ProductPersistencePort;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class ProductServiceImpl implements ProductUseCase {

    private final ProductPersistencePort productPersistencePort;


    @Autowired
    public ProductServiceImpl(ProductPersistencePort productPersistencePort) {
        this.productPersistencePort = productPersistencePort;
    }

    @Override
    public Page<ProductModel> getAll(Pageable pageable) {
        return this.productPersistencePort.findAll(pageable);
    }

    @Override
    public ProductModel getById(Long id) {
        return null;
    }

    @Override
    public ProductModel getByName(String name) {
        return null;
    }

    @Override
    public void deleteProduct(Long id) {

    }

    @Override
    public ProductModel editProduct(ProductModel productModel, Long id) {
        return null;
    }

    @Override
    @Transactional
    public ProductModel save(@Valid ProductModel productModel) {
        return this.productPersistencePort.save(productModel);
    }
}


