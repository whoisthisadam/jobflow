package com.app.salary;

import com.app.appUser.AppUser;
import com.app.appUser.UserRepository;
import com.app.system.exception.BadRequestException;
import com.app.system.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserSalaryService {

    private final UserSalaryRepository repository;
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

    /**
     * Find all salary records
     *
     * @return List of all salary records
     */
    public List<UserSalary> findAll() {
        return repository.findAll();
    }

    /**
     * Find a salary record by ID
     *
     * @param id The ID of the salary record
     * @return The salary record
     * @throws ObjectNotFoundException if the record is not found
     */
    public UserSalary find(String id) {
        try {
            Long longId = Long.parseLong(id);
            return repository.findById(longId)
                    .orElseThrow(() -> new ObjectNotFoundException("Запись о зарплате с ID " + id + " не найдена"));
        } catch (NumberFormatException e) {
            throw new BadRequestException("Некорректный ID: " + id);
        }
    }

    /**
     * Find all salary records for a specific user
     *
     * @param userId The ID of the user
     * @return List of salary records for the user
     */
    public List<UserSalary> findByUser(String userId) {
        AppUser user = findUser(userId);
        return repository.findByUser(user);
    }

    /**
     * Find all salary records for the current user
     *
     * @return List of salary records for the current user
     */
    public List<UserSalary> findByCurrentUser() {
        AppUser currentUser = getCurrentUser();
        return repository.findByUser(currentUser);
    }

    /**
     * Find a specific salary record for a user by year and month
     *
     * @param userId The ID of the user
     * @param year The year of the salary record
     * @param month The month of the salary record
     * @return The salary record
     * @throws ObjectNotFoundException if the record is not found
     */
    public UserSalary findByUserAndYearAndMonth(String userId, Integer year, Integer month) {
        AppUser user = findUser(userId);
        return repository.findByUserAndYearAndMonth(user, year, month)
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Запись о зарплате для пользователя " + user.getFio() +
                        " за " + month + "." + year + " не найдена"));
    }

    /**
     * Find a specific salary record for the current user by year and month
     *
     * @param year The year of the salary record
     * @param month The month of the salary record
     * @return The salary record or null if not found
     */
    public Optional<UserSalary> findByCurrentUserAndYearAndMonth(Integer year, Integer month) {
        AppUser currentUser = getCurrentUser();
        return repository.findByUserAndYearAndMonth(currentUser, year, month);
    }

    /**
     * Save a new salary record
     *
     * @param userSalary The salary record to save
     * @return The saved salary record
     * @throws BadRequestException if a record already exists for the user, year, and month
     */
    public UserSalary save(UserSalary userSalary) {
        // Check if a record already exists for this user, year, and month
        if (repository.existsByUserAndYearAndMonth(userSalary.getUser(), userSalary.getYear(), userSalary.getMonth())) {
            throw new BadRequestException(
                    "Запись о зарплате для пользователя " + userSalary.getUser().getFio() +
                    " за " + userSalary.getMonth() + "." + userSalary.getYear() + " уже существует");
        }

        // Set the created by user if not already set
        if (userSalary.getCreatedBy() == null) {
            userSalary.setCreatedBy(getCurrentUser());
        }

        // Calculate total salary
        userSalary.calculateTotalSalary();

        return repository.save(userSalary);
    }

    /**
     * Update an existing salary record
     *
     * @param userSalary The updated salary record
     * @return The updated salary record
     * @throws ObjectNotFoundException if the record is not found
     */
    public UserSalary update(UserSalary userSalary) {
        // Check if the record exists
        UserSalary existingSalary = repository.findById(userSalary.getId())
                .orElseThrow(() -> new ObjectNotFoundException("Запись о зарплате с ID " + userSalary.getId() + " не найдена"));

        // Update fields
        existingSalary.setBaseSalary(userSalary.getBaseSalary());
        existingSalary.setTaskIntensityBonus(userSalary.getTaskIntensityBonus());
        existingSalary.setExperienceBonus(userSalary.getExperienceBonus());
        existingSalary.setIncomeTax(userSalary.getIncomeTax());
        existingSalary.setCppTax(userSalary.getCppTax());
        existingSalary.setOtherDeductions(userSalary.getOtherDeductions());
        existingSalary.setUpdatedAt(LocalDateTime.now());

        // Calculate total salary
        existingSalary.calculateTotalSalary();

        return repository.save(existingSalary);
    }

    /**
     * Delete a salary record by ID
     *
     * @param id The ID of the salary record to delete
     */
    public void deleteById(String id) {
        UserSalary userSalary = find(id);
        repository.deleteById(userSalary.getId());
    }
}
