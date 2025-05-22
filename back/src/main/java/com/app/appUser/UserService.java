package com.app.appUser;

import com.app.category.CategoryService;
import com.app.enums.Role;
import com.app.salary.UserSalary;
import com.app.salary.UserSalaryRepository;
import com.app.system.exception.BadRequestException;
import com.app.system.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.app.util.Global.saveFile;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final CategoryService categoryService;
    private final UserSalaryRepository userSalaryRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username)
                .map(MyUserPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с логином " + username + " не найден"));
    }

    public AppUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            return repository.findByUsername(currentUserName).orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден"));
        }
        return null;
    }

    public List<AppUser> findAll() {
        return repository.findAll();
    }

    public List<AppUser> findAllByRoleUser() {
        return repository.findAllByRole(Role.USER);
    }

    public AppUser find(String id) {
        try {
            Long longId = Long.parseLong(id);
            return repository.findById(longId).orElseThrow();
        } catch (Exception e) {
            throw new ObjectNotFoundException("Не найден пользователь с ИД: " + id);
        }
    }

    public Object findIncome(String date) {
        AppUser user = getCurrentUser();

        // If date is empty, return the total income
        if (date.isEmpty()) {
            return user.getIncome();
        }

        // Parse year and month from date string (format: YYYY-MM)
        try {
            String[] parts = date.split("-");
            if (parts.length >= 2) {
                int year = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);

                // Check if there's a salary record for this year and month
                Optional<UserSalary> salaryOpt = userSalaryRepository.findByUserAndYearAndMonth(getCurrentUser(), year, month);

                if (salaryOpt.isPresent()) {
                    // Return the total salary from the record
                    return salaryOpt.get().getTotalSalary();
                } else {
                    // No salary record found, return a special value to indicate this
                    return "Зарплата за " + month + "." + year + " еще не установлена";
                }
            }
        } catch (Exception e) {
            // If there's an error parsing the date, fall back to the old calculation
        }

        // Fall back to the old calculation method
        return user.getIncome(date);
    }

    public AppUser save(AppUser user) {
        if (repository.findByUsername(user.getUsername()).isPresent()) {
            throw new BadRequestException("Пользователь с таким логином уже существует");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (findAll().isEmpty()) {
            user.setRole(Role.ADMIN);
        }
        return repository.save(user);
    }

    public AppUser update(AppUser user) {
        AppUser old = getCurrentUser();
        old.set(user);
        return repository.save(old);
    }

    public AppUser updateImg(MultipartFile img) {
        AppUser user = getCurrentUser();
        try {
            user.setImg(saveFile(img, "user"));
        } catch (IOException e) {
            throw new BadRequestException("Некорректное изображение");
        }
        return repository.save(user);
    }

    public AppUser updateFio(String fio, int exp) {
        AppUser user = getCurrentUser();
        user.setFio(fio);
        user.setExp(exp);
        return repository.save(user);
    }

    public AppUser updateRole(String id, String role) {
        AppUser user = find(id);
        try {
            user.setRole(Role.valueOf(role));
        } catch (Exception e) {
            throw new BadRequestException("Некорректный выбор роли");
        }
        return repository.save(user);
    }

    public AppUser updateCategory(String id, String categoryId) {
        AppUser user = find(id);
        user.setCategory(categoryService.find(categoryId));
        return repository.save(user);
    }

    public AppUser updateExp(String id, int exp) {
        AppUser user = find(id);
        user.setExp(exp);
        return repository.save(user);
    }

    public void deleteById(String userId) {
        AppUser user = find(userId);
        repository.deleteById(user.getId());
    }

}
