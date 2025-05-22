package com.app.salary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSalaryDto {
    private Long id;
    private Long userId;
    private String userFio;
    private Integer year;
    private Integer month;
    private String monthName;
    private Float baseSalary;
    private Float taskIntensityBonus;
    private Float experienceBonus;
    private Float incomeTax;
    private Float cppTax;
    private Float otherDeductions;
    private Float totalSalary;
    private Long createdById;
    private String createdByFio;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
