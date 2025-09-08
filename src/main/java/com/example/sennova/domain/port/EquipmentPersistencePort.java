package com.example.sennova.domain.port;

import com.example.sennova.domain.model.EquipmentLocationModel;
import com.example.sennova.domain.model.EquipmentModel;
import com.example.sennova.domain.model.EquipmentUsageModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EquipmentPersistencePort {

    EquipmentModel save(EquipmentModel equipmentModel);
    EquipmentModel update(Long id, EquipmentModel equipmentModel);
    Page<EquipmentModel> getAllPage(Pageable pageable);
    EquipmentModel findById(Long id);
    EquipmentModel findByInternalCode(Long internalCode);
    List<EquipmentModel> findAllByName(String name);
    Boolean existById(Long id);
    void changeState(Long id, String state);
    List<EquipmentModel> findAllByLocation(EquipmentLocationModel equipmentLocationModel);
    List<EquipmentModel> findAllByUsage(EquipmentUsageModel equipmentUsageModel);
    void delete(Long id);
    Boolean existsBySerialNumber(Long serialNumber);
    Boolean existsByInternalCode(String internalCode);

    // add the method for get All detail information

}
