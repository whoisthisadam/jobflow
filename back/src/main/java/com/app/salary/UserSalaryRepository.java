package com.app.salary;

import com.app.appUser.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSalaryRepository extends JpaRepository<UserSalary, Long> {
    
    /**
     * Find all salary records for a specific user
     * 
     * @param user The user to find salary records for
     * @return List of salary records
     */
    List<UserSalary> findByUser(AppUser user);
    
    /**
     * Find a specific salary record for a user by year and month
     * 
     * @param user The user to find the salary record for
     * @param year The year of the salary record
     * @param month The month of the salary record
     * @return Optional containing the salary record if found
     */
    Optional<UserSalary> findByUserAndYearAndMonth(AppUser user, Integer year, Integer month);
    
    /**
     * Check if a salary record exists for a user in a specific year and month
     * 
     * @param user The user to check
     * @param year The year to check
     * @param month The month to check
     * @return True if a record exists, false otherwise
     */
    boolean existsByUserAndYearAndMonth(AppUser user, Integer year, Integer month);
}
