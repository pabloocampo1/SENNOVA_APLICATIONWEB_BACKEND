package com.example.sennova.application.usecasesImpl;

import com.example.sennova.application.dto.EquipmentInventory.EquipmentLoanRequest;
import com.example.sennova.application.usecases.EquipmentLoanUseCase;
import com.example.sennova.application.usecases.EquipmentUseCase;
import com.example.sennova.domain.model.EquipmentLoanModel;
import com.example.sennova.domain.model.EquipmentModel;
import com.example.sennova.domain.port.EquipmentLoanPersistencePort;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentLoanServiceImp implements EquipmentLoanUseCase {

    private final EquipmentLoanPersistencePort equipmentLoanPersistencePort;
    private final EquipmentUseCase equipmentUseCase;

    @Autowired
    public EquipmentLoanServiceImp(EquipmentLoanPersistencePort equipmentLoanPersistencePort, EquipmentUseCase equipmentUseCase) {
        this.equipmentLoanPersistencePort = equipmentLoanPersistencePort;
        this.equipmentUseCase = equipmentUseCase;
    }

    @Override
    public EquipmentLoanModel save(@Valid EquipmentLoanRequest equipmentLoanRequest) {

        EquipmentModel equipmentModel = this.equipmentUseCase.getById(equipmentLoanRequest.equipmentId());

        EquipmentLoanModel equipmentLoanModel = new EquipmentLoanModel();
        equipmentLoanModel.setEquipment(equipmentModel);
        equipmentLoanModel.setNameLoan(equipmentLoanRequest.nameLoan());
        equipmentLoanModel.setNotes(equipmentLoanRequest.notes());
        equipmentLoanModel.setLoanDate(equipmentLoanRequest.loanDate());
        equipmentLoanModel.setType(equipmentLoanRequest.type());

        System.out.println("el que se pasa para guardar: " + equipmentLoanModel);


        return this.equipmentLoanPersistencePort.save(equipmentLoanModel);
    }

    @Override
    public List<EquipmentLoanModel> getAllByEquipmentId(@Valid Long equipmentId) {
        try{
            System.out.println("llego al service");
            return this.equipmentLoanPersistencePort.findAllByEquipmentId(equipmentId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}
