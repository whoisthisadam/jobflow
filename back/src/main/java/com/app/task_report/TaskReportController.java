package com.app.task_report;

import com.app.system.Result;
import com.app.system.StatusCode;
import com.app.task_report.converter.TaskReportDtoToTaskReportConverter;
import com.app.task_report.converter.TaskReportToTaskReportDtoConverter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.app.util.Global.USER;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks/reports")
@Secured({USER})
public class TaskReportController {

    private final TaskReportService service;
    private final TaskReportToTaskReportDtoConverter toDtoConverter;
    private final TaskReportDtoToTaskReportConverter toConverter;

    @PostMapping
    public Result save(@Valid @RequestBody TaskReportDto saveDto, @RequestParam String taskId) {
        TaskReport save = toConverter.convert(saveDto);
        TaskReport saved = service.save(save, taskId);
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Save",
                toDtoConverter.convert(saved)
        );
    }

    @PatchMapping("/{id}/img")
    public Result updateImg(@PathVariable String id, @RequestParam MultipartFile file) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Update Img",
                toDtoConverter.convert(service.updateImg(id, file))
        );
    }

    @PatchMapping("/{id}/file")
    public Result updateFile(@PathVariable String id, @RequestParam MultipartFile file) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Update File",
                toDtoConverter.convert(service.updateFile(id, file))
        );
    }

}

