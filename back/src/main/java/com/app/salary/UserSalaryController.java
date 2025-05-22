package com.app.salary;

import com.app.salary.converter.UserSalaryDtoToUserSalaryConverter;
import com.app.salary.converter.UserSalaryToUserSalaryDtoConverter;
import com.app.system.Result;
import com.app.system.StatusCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;

import static com.app.util.Global.*;

@RestController
@RequestMapping("/salary")
@RequiredArgsConstructor
public class UserSalaryController {

    private final UserSalaryService service;
    private final UserSalaryToUserSalaryDtoConverter toDtoConverter;
    private final UserSalaryDtoToUserSalaryConverter toEntityConverter;

    @Secured({ADMIN, MANAGER})
    @GetMapping("/all")
    public Result findAll() {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Find All",
                service.findAll().stream().map(toDtoConverter::convert).collect(Collectors.toList())
        );
    }

    @Secured({ADMIN, MANAGER})
    @GetMapping("/user/{id}")
    public Result findByUser(@PathVariable String id) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Find By User",
                service.findByUser(id).stream().map(toDtoConverter::convert).collect(Collectors.toList())
        );
    }

    @Secured({ADMIN, MANAGER, USER})
    @GetMapping("/current")
    public Result findByCurrentUser() {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Find By Current User",
                service.findByCurrentUser().stream().map(toDtoConverter::convert).collect(Collectors.toList())
        );
    }

    @Secured({ADMIN, MANAGER})
    @GetMapping("/user/{id}/year/{year}/month/{month}")
    public Result findByUserAndYearAndMonth(
            @PathVariable String id,
            @PathVariable Integer year,
            @PathVariable Integer month) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Find By User, Year, and Month",
                toDtoConverter.convert(service.findByUserAndYearAndMonth(id, year, month))
        );
    }

    @Secured({ADMIN, MANAGER, USER})
    @GetMapping("/current/year/{year}/month/{month}")
    public Result findByCurrentUserAndYearAndMonth(
            @PathVariable Integer year,
            @PathVariable Integer month) {
        Optional<UserSalary> userSalaryOptional = service.findByCurrentUserAndYearAndMonth(year, month);

        return userSalaryOptional.map(userSalary -> new Result(
                true,
                StatusCode.SUCCESS,
                "Success Find By Current User, Year, and Month",
                toDtoConverter.convert(userSalary)
        )).orElseGet(() -> new Result(
                false,
                StatusCode.NOT_FOUND,
                "Зарплата за " + getMonthName(month) + " " + year + " еще не установлена",
                null
        ));
    }

    @Secured({ADMIN, MANAGER})
    @PostMapping
    public Result save(@Valid @RequestBody UserSalaryDto userSalaryDto) {
        UserSalary userSalary = toEntityConverter.convert(userSalaryDto);
        UserSalary saved = service.save(userSalary);
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Save",
                toDtoConverter.convert(saved)
        );
    }

    @Secured({ADMIN, MANAGER})
    @PutMapping
    public Result update(@Valid @RequestBody UserSalaryDto userSalaryDto) {
        if (userSalaryDto.getId() == null) {
            return new Result(
                    false,
                    StatusCode.INVALID_ARGUMENT,
                    "ID is required for update",
                    null
            );
        }
        
        UserSalary userSalary = toEntityConverter.convert(userSalaryDto);
        UserSalary updated = service.update(userSalary);
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Update",
                toDtoConverter.convert(updated)
        );
    }

    @Secured({ADMIN})
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable String id) {
        service.deleteById(id);
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Delete"
        );
    }
}
