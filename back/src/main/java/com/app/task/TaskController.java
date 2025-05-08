package com.app.task;

import com.app.system.Result;
import com.app.system.StatusCode;
import com.app.task.converter.TaskDtoToTaskConverter;
import com.app.task.converter.TaskToTaskDtoConverter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

import static com.app.util.Global.MANAGER;
import static com.app.util.Global.USER;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService service;
    private final TaskToTaskDtoConverter toDtoConverter;
    private final TaskDtoToTaskConverter toConverter;

    @Secured({MANAGER, USER})
    @GetMapping
    public Result findAll() {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Find All",
                service.findAll().stream().map(toDtoConverter::convert).collect(Collectors.toList())
        );
    }

    @Secured({MANAGER, USER})
    @GetMapping("/{id}")
    public Result find(@PathVariable String id) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Find",
                toDtoConverter.convert(service.find(id))
        );
    }

    @Secured({MANAGER})
    @PostMapping
    public Result save(@Valid @RequestBody TaskDto saveDto, @RequestParam String userId) {
        Task save = toConverter.convert(saveDto);
        Task saved = service.save(save, userId);
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Save",
                toDtoConverter.convert(saved)
        );
    }

    @Secured({USER})
    @PatchMapping("/{id}/work")
    public Result work(@PathVariable String id) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Work",
                toDtoConverter.convert(service.work(id))
        );
    }

    @Secured({USER})
    @PatchMapping("/{id}/done")
    public Result done(@PathVariable String id) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Done",
                toDtoConverter.convert(service.done(id))
        );
    }

}
