package com.example.sennova.application.usecasesImpl;

import com.example.sennova.application.usecases.UsageEquipmentUseCase;
import com.example.sennova.domain.model.EquipmentUsageModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsageEquipmentServiceImpl implements UsageEquipmentUseCase {
    @Override
    public EquipmentUsageModel save(EquipmentUsageModel equipmentUsageModel) {
        return null;
    }

    @Override
    public EquipmentUsageModel update(Long id, EquipmentUsageModel equipmentUsageModel) {
        return null;
    }

    @Override
    public List<EquipmentUsageModel> getAllByName(String name) {
        return List.of();
    }

    @Override
    public EquipmentUsageModel getById(Long id) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }
}
