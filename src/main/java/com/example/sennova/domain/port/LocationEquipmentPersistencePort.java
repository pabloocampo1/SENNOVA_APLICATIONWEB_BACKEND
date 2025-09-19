package com.example.sennova.domain.port;

import com.example.sennova.domain.model.EquipmentLocationModel;
import com.example.sennova.domain.model.EquipmentModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LocationEquipmentPersistencePort {
    EquipmentLocationModel save(EquipmentLocationModel equipmentLocationModel);
    EquipmentLocationModel findById(Long id);
    EquipmentLocationModel update(EquipmentLocationModel equipmentLocationModel, Long id);
    Page<EquipmentLocationModel> findAllPage(Pageable pageable);
    void deleteById(Long id);
    Boolean existById(Long id);
    List<EquipmentLocationModel> findAllByName(String name);
    List<EquipmentLocationModel> findAll();

}
