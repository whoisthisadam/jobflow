package com.app.task_report.converter;

import com.app.task_report.TaskReport;
import com.app.task_report.TaskReportDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TaskReportDtoToTaskReportConverter implements Converter<TaskReportDto, TaskReport> {
    @Override
    public TaskReport convert(TaskReportDto source) {
        return new TaskReport(
                source.name()
        );
    }
}
