package com.example.sennova.infrastructure.mapperDbo;

import com.example.sennova.domain.model.EquipmentUsageModel;
import com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities.UsageEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsageEquipmentMapperDbo {

    UsageEntity toEntity(EquipmentUsageModel equipmentUsageModel);
    EquipmentUsageModel toModel(UsageEntity usageEntity);
}
