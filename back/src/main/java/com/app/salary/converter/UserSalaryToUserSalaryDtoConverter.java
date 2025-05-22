package com.app.salary.converter;

import com.app.salary.UserSalary;
import com.app.salary.UserSalaryDto;
import com.app.util.Global;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserSalaryToUserSalaryDtoConverter implements Converter<UserSalary, UserSalaryDto> {

    @Override
    public UserSalaryDto convert(UserSalary source) {
        UserSalaryDto dto = new UserSalaryDto();
        
        dto.setId(source.getId());
        dto.setUserId(source.getUser().getId());
        dto.setUserFio(source.getUser().getFio());
        dto.setYear(source.getYear());
        dto.setMonth(source.getMonth());
        dto.setMonthName(Global.getMonthName(source.getMonth()));
        dto.setBaseSalary(source.getBaseSalary());
        dto.setTaskIntensityBonus(source.getTaskIntensityBonus());
        dto.setExperienceBonus(source.getExperienceBonus());
        dto.setIncomeTax(source.getIncomeTax());
        dto.setCppTax(source.getCppTax());
        dto.setOtherDeductions(source.getOtherDeductions());
        dto.setTotalSalary(source.getTotalSalary());
        dto.setCreatedById(source.getCreatedBy().getId());
        dto.setCreatedByFio(source.getCreatedBy().getFio());
        dto.setCreatedAt(source.getCreatedAt());
        dto.setUpdatedAt(source.getUpdatedAt());
        
        return dto;
    }
}
