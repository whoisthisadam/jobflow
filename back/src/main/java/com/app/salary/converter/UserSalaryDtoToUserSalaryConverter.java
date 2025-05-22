package com.app.salary.converter;

import com.app.appUser.AppUser;
import com.app.appUser.UserRepository;
import com.app.salary.UserSalary;
import com.app.salary.UserSalaryDto;
import com.app.system.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserSalaryDtoToUserSalaryConverter implements Converter<UserSalaryDto, UserSalary> {

    private final UserRepository userRepository;

    /**
     * Get the current user from the security context
     *
     * @return The current user
     * @throws ObjectNotFoundException if the user is not found
     */
    private AppUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            return userRepository.findByUsername(currentUserName)
                    .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден"));
        }
        return null;
    }

    /**
     * Find a user by ID
     *
     * @param id The ID of the user
     * @return The user
     * @throws ObjectNotFoundException if the user is not found
     */
    private AppUser findUser(String id) {
        try {
            Long longId = Long.parseLong(id);
            return userRepository.findById(longId)
                    .orElseThrow(() -> new ObjectNotFoundException("Не найден пользователь с ИД: " + id));
        } catch (Exception e) {
            throw new ObjectNotFoundException("Не найден пользователь с ИД: " + id);
        }
    }

    @Override
    public UserSalary convert(UserSalaryDto source) {
        UserSalary userSalary = new UserSalary();

        if (source.getId() != null) {
            userSalary.setId(source.getId());
        }

        // Set user
        if (source.getUserId() != null) {
            AppUser user = findUser(source.getUserId().toString());
            userSalary.setUser(user);
        }

        userSalary.setYear(source.getYear());
        userSalary.setMonth(source.getMonth());
        userSalary.setBaseSalary(source.getBaseSalary());
        userSalary.setTaskIntensityBonus(source.getTaskIntensityBonus());
        userSalary.setExperienceBonus(source.getExperienceBonus());
        userSalary.setIncomeTax(source.getIncomeTax());
        userSalary.setCppTax(source.getCppTax());
        userSalary.setOtherDeductions(source.getOtherDeductions());

        // Set created by
        if (source.getCreatedById() != null) {
            AppUser createdBy = findUser(source.getCreatedById().toString());
            userSalary.setCreatedBy(createdBy);
        } else {
            // If not specified, use the current user
            userSalary.setCreatedBy(getCurrentUser());
        }

        // Calculate total salary
        userSalary.calculateTotalSalary();

        return userSalary;
    }
}
