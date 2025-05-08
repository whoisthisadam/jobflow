package com.app.task.converter;

import com.app.task.Task;
import com.app.task.TaskDto;
import com.app.task_report.converter.TaskReportToTaskReportDtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TaskToTaskDtoConverter implements Converter<Task, TaskDto> {

    private final TaskReportToTaskReportDtoConverter taskReportToTaskReportDtoConverter;

    @Override
    public TaskDto convert(Task source) {
        return new TaskDto(
                source.getId(),

                source.getName(),

                source.getDate(),
                source.getDateFormatted(),

                source.getIntensity(),
                source.getAddress(),

                source.getDateEnd(),
                source.getDateEndFormatted(),

                source.getTime(),

                source.getDescription(),

                source.getStatus().name(),
                source.getStatus().getName(),

                source.getUser().getId(),
                source.getUser().getFio(),
                source.getUser().getImg(),

                source.getReports().stream().map(taskReportToTaskReportDtoConverter::convert).collect(Collectors.toList())
        );
    }
}
