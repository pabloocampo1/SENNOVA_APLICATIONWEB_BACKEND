package com.example.sennova.domain.model;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReagentModel {

    private Long reagentsId;

    private String reagentName;

    private String brand;

    private String purity;

    private Integer units;

    private Integer quantity;

    private String measurementUnit;

    private String batch;

    private LocalDate expirationDate;

    private LocalDate createAt;

    private LocalDate updateAt;

    private UserModel userModel;






}
