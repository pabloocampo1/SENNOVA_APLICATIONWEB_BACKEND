package com.example.sennova.infrastructure.adpaters;

import com.example.sennova.domain.model.EquipmentLoanModel;
import com.example.sennova.domain.port.EquipmentLoanPersistencePort;
import com.example.sennova.infrastructure.mapperDbo.EquipmentLoanMapperDbo;
import com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities.EquipmentEntity;
import com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities.EquipmentLoanEntity;
import com.example.sennova.infrastructure.persistence.repositoryJpa.EquipmentLoanRepositoryJpa;
import com.example.sennova.infrastructure.persistence.repositoryJpa.EquipmentMediaRepositoryJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EquipmentLoanAdapterImpl implements EquipmentLoanPersistencePort {

    private final EquipmentLoanRepositoryJpa equipmentLoanRepositoryJpa;
    private final EquipmentLoanMapperDbo equipmentLoanMapperDbo;


    @Autowired
    public EquipmentLoanAdapterImpl(EquipmentLoanRepositoryJpa equipmentLoanRepositoryJpa, EquipmentLoanMapperDbo equipmentLoanMapperDbo) {
        this.equipmentLoanRepositoryJpa = equipmentLoanRepositoryJpa;
        this.equipmentLoanMapperDbo = equipmentLoanMapperDbo;
    }


    @Override
    public EquipmentLoanModel save(EquipmentLoanModel equipmentLoanModel) {
        EquipmentLoanEntity equipmentLoanEntity = this.equipmentLoanMapperDbo.toEntity(equipmentLoanModel);
        System.out.println("entity" + equipmentLoanEntity);
        EquipmentLoanEntity equipmentLoanSaved = this.equipmentLoanRepositoryJpa.save(equipmentLoanEntity);
        return this.equipmentLoanMapperDbo.toModel(equipmentLoanSaved);
    }

    @Override
    public List<EquipmentLoanModel> findAllByEquipmentId(Long equipmentId) {
        try {
            System.out.println("llego aca 1");
            List<EquipmentLoanEntity> equipmentLoanEntityList = this.equipmentLoanRepositoryJpa.findByEquipment_EquipmentId(equipmentId);
            System.out.println("adapater");
            return equipmentLoanEntityList.stream().map(this.equipmentLoanMapperDbo::toModel).toList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
