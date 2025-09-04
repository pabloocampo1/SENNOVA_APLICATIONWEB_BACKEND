package com.example.sennova.domain.port;

import com.example.sennova.application.dto.productDtos.ProductRequestDto;
import com.example.sennova.domain.model.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductPersistencePort {
    Page<ProductModel> findAll(Pageable pageable);
    ProductModel findById(Long id);
    ProductModel findByName(String name);
    void deleteById(Long id);
    ProductModel update(ProductModel productModel, Long id);
    ProductModel save(ProductModel productModel);
}
