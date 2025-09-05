package com.example.sennova.infrastructure.mapperDbo;

import com.example.sennova.domain.model.EquipmentUsageModel;
import com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities.EquipmentUsageEntity;
import com.example.sennova.infrastructure.persistence.entities.inventoryReagentsEntities.UsageReagentsEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsageEquipmentMapperDbo {

    EquipmentUsageEntity toEntity(EquipmentUsageModel equipmentUsageModel);
    EquipmentUsageModel toModel(EquipmentUsageEntity equipmentUsageEntity);
}
