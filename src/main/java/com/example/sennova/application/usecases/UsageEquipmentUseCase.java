package com.example.sennova.application.usecases;

import com.example.sennova.domain.model.EquipmentUsageModel;
import com.example.sennova.domain.model.EquipmentLocationModel;

import java.util.List;

public interface UsageEquipmentUseCase {
    EquipmentUsageModel save(EquipmentUsageModel equipmentUsageModel);
    EquipmentUsageModel update(Long id, EquipmentUsageModel equipmentUsageModel);
    List<EquipmentUsageModel> getAllByName(String name);
    EquipmentUsageModel getById(Long id);
    void deleteById(Long id);
}
