package com.example.sennova.infrastructure.mapperDbo;

import com.example.sennova.domain.model.EquipmentLocationModel;
import com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities.LocationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocationEquipmentMapperDbo {

    EquipmentLocationModel toModel(LocationEntity locationEntity);
    LocationEntity toEntity(EquipmentLocationModel equipmentLocationModel);
}
