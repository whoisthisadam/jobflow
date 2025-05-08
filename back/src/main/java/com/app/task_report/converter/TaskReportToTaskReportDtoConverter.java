package com.app.task_report.converter;

import com.app.task_report.TaskReport;
import com.app.task_report.TaskReportDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TaskReportToTaskReportDtoConverter implements Converter<TaskReport, TaskReportDto> {
    @Override
    public TaskReportDto convert(TaskReport source) {
        return new TaskReportDto(
                source.getId(),

                source.getName(),

                source.getDate(),
                source.getDateFormatted(),

                source.getImg(),
                source.getFile()
        );
    }
}
