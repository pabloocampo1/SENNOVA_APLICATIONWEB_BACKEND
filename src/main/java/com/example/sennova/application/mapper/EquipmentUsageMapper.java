package com.example.sennova.application.mapper;

import com.example.sennova.application.dto.EquipmentInventory.request.UsageEquipmentRequestDto;
import com.example.sennova.application.dto.EquipmentInventory.response.UsageEquipmentResponseDto;
import com.example.sennova.domain.model.EquipmentUsageModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EquipmentUsageMapper {
    UsageEquipmentResponseDto toResponse(EquipmentUsageModel equipmentUsageModel);
    EquipmentUsageModel toModel(UsageEquipmentRequestDto usageEquipmentRequestDto);
}
