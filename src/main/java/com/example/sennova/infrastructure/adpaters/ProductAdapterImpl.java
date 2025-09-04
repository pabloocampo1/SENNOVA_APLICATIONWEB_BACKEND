package com.example.sennova.infrastructure.adpaters;

import com.example.sennova.application.dto.productDtos.ProductRequestDto;
import com.example.sennova.domain.model.ProductModel;
import com.example.sennova.domain.port.ProductPersistencePort;
import com.example.sennova.infrastructure.mapperDbo.ProductMapperDbo;
import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.ProductEntity;
import com.example.sennova.infrastructure.persistence.repositoryJpa.ProductRepositoryJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class ProductAdapterImpl implements ProductPersistencePort {

    private final ProductRepositoryJpa productRepositoryJpa;
    private final ProductMapperDbo productMapperDbo;

     @Autowired
    public ProductAdapterImpl(ProductRepositoryJpa productRepositoryJpa, ProductMapperDbo productMapperDbo) {
        this.productRepositoryJpa = productRepositoryJpa;
         this.productMapperDbo = productMapperDbo;
     }


    @Override
    public Page<ProductModel> findAll(Pageable pageable) {
         Page<ProductEntity> productEntityPage = this.productRepositoryJpa.findAll(pageable);
        return productEntityPage.map(this.productMapperDbo::toModel);
    }

    @Override
    public ProductModel findById(Long id) {
        return null;
    }

    @Override
    public ProductModel findByName(String name) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public ProductModel update(ProductModel productModel, Long id) {
        return null;
    }

    @Override
    public ProductModel save(ProductModel productModel) {
        ProductEntity productEntity = this.productMapperDbo.toEntity(productModel);
        ProductEntity productSaved = this.productRepositoryJpa.save(productEntity);
        return this.productMapperDbo.toModel(productSaved);
    }
}
