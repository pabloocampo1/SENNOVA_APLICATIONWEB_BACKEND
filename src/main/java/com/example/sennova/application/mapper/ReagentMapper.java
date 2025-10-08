package com.example.sennova.application.mapper;

import com.example.sennova.application.dto.inventory.ReagentInventory.ReagentRequestDto;
import com.example.sennova.application.dto.inventory.ReagentInventory.ReagentResponseDto;
import com.example.sennova.domain.model.ReagentModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReagentMapper {

    ReagentResponseDto toResponse(ReagentModel reagentModel);
    ReagentModel toModel(ReagentRequestDto reagentRequestDto);
}
