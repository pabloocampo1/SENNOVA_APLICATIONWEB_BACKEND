package com.example.sennova.application.usecases;

import com.example.sennova.domain.model.ReagentModel;
import com.example.sennova.infrastructure.persistence.entities.inventoryReagentsEntities.ReagentsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReagentUseCase {
    ReagentModel save(ReagentModel reagentModel, MultipartFile multipartFile, Long userId, Long responsibleId, Long locationId, Long usageId);
    ReagentModel update(ReagentModel reagentModel, Long reagentId, MultipartFile multipartFile);
    ReagentModel getById(Long id);
    ReagentsEntity getEntity(Long id);
    List<ReagentModel> getAll();
    Page<ReagentModel> getAll(Pageable pageable);
    List<ReagentModel> getAllByName(String name);
    List<ReagentModel> getAllByLocation(Long locationId);
    void deleteById(Long id);
}
