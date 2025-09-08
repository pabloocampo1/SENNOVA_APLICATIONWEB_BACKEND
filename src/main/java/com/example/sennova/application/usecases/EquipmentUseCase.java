package com.example.sennova.application.usecases;

import com.example.sennova.domain.model.EquipmentLocationModel;
import com.example.sennova.domain.model.EquipmentModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EquipmentUseCase {
    EquipmentModel save(EquipmentModel equipmentModel,Long responsibleId, Long locationId, Long usageId);
    EquipmentModel update(EquipmentModel equipmentModel, Long id);
    EquipmentModel getById(Long id);
    List<EquipmentModel> getAllById(String name);
    Page<EquipmentModel> getAll(Pageable pageable);
    List<EquipmentModel> getAll();
    void deleteById(Long id);
    List<EquipmentModel> getByLocation(Long locationId);
    List<EquipmentModel> getByUsage(Long usageId);
    void changeState(Long id, String state);


}
