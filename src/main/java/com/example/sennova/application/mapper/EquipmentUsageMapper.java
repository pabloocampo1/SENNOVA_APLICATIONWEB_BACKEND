package com.example.sennova.application.mapper;

import com.example.sennova.application.dto.EquipmentInventory.LocationEquipmentRequestDto;
import com.example.sennova.application.dto.EquipmentInventory.LocationEquipmentResponseDto;
import com.example.sennova.application.dto.EquipmentInventory.UsageEquipmentRequestDto;
import com.example.sennova.application.dto.EquipmentInventory.UsageEquipmentResponseDto;
import com.example.sennova.domain.model.EquipmentLocationModel;
import com.example.sennova.domain.model.EquipmentUsageModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EquipmentUsageMapper {
    UsageEquipmentResponseDto toResponse(EquipmentUsageModel equipmentUsageModel);
    EquipmentUsageModel toModel(UsageEquipmentRequestDto usageEquipmentRequestDto);
}
