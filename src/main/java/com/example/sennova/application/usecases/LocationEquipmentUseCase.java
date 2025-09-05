package com.example.sennova.application.usecases;

import com.example.sennova.domain.model.EquipmentLocationModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LocationEquipmentUseCase {
    EquipmentLocationModel save(EquipmentLocationModel equipmentLocationModel);
    EquipmentLocationModel update(Long id, EquipmentLocationModel equipmentLocationModel);
    List<EquipmentLocationModel> getAllByName(String name);
    EquipmentLocationModel getById(Long id);
    void deleteById(Long id);
    Page<EquipmentLocationModel> getAllPage(Pageable pageable);

}
