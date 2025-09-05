package com.example.sennova.domain.port;

import com.example.sennova.domain.model.EquipmentUsageModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UsageEquipmentPersistencePort {
    EquipmentUsageModel save(EquipmentUsageModel equipmentUsageModel);
    EquipmentUsageModel update(EquipmentUsageModel equipmentUsageModel,Long id);
    List<EquipmentUsageModel> findAll();
    Page<EquipmentUsageModel>findAll(Pageable pageable);
    boolean existsById(Long id);
    List<EquipmentUsageModel> findAllByName(String name);
    EquipmentUsageModel findById(Long id);
    void deleteById(Long id);

}
