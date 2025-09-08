package com.example.sennova.application.mapper;

import com.example.sennova.application.dto.EquipmentInventory.EquipmentRequestDto;
import com.example.sennova.application.dto.EquipmentInventory.EquipmentResponseDto;
import com.example.sennova.domain.model.EquipmentModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EquipmentMapper {

    EquipmentResponseDto toResponse(EquipmentModel equipmentModel);
    EquipmentModel toDomain(EquipmentRequestDto equipmentRequestDto);
}
