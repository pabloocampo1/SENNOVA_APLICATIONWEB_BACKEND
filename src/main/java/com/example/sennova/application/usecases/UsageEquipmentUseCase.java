package com.example.sennova.application.usecases;

import com.example.sennova.domain.model.EquipmentUsageModel;
import com.example.sennova.domain.model.EquipmentLocationModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UsageEquipmentUseCase {
    EquipmentUsageModel save(EquipmentUsageModel equipmentUsageModel);
    EquipmentUsageModel update(Long id, EquipmentUsageModel equipmentUsageModel);
    List<EquipmentUsageModel> getAllByName(String name);
    Page<EquipmentUsageModel> getAll(Pageable pageable);
    List<EquipmentUsageModel> getAll();
    EquipmentUsageModel getById(Long id);
    void deleteById(Long id);
}
