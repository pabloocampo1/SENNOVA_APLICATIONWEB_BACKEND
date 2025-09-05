package com.example.sennova.application.mapper;

import com.example.sennova.application.dto.EquipmentInventory.LocationEquipmentRequestDto;
import com.example.sennova.application.dto.EquipmentInventory.LocationEquipmentResponseDto;
import com.example.sennova.domain.model.EquipmentLocationModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EquipmentLocationMapper {

    LocationEquipmentResponseDto toResponse(EquipmentLocationModel equipmentLocationModel);
    EquipmentLocationModel toModel(LocationEquipmentRequestDto locationEquipmentRequestDto);
}
