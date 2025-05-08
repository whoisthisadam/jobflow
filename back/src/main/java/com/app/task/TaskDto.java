package com.app.task;

import com.app.task_report.TaskReportDto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record TaskDto(
        Long id,

        @Size(min = 1, max = 255, message = "name is required length 1-255")
        @NotEmpty(message = "name is required")
        String name,

        String date,
        String dateFormatted,

        @Min(value = 1, message = "sum is required min 1")
        @Max(value = 1000000, message = "sum is required min 1000000")
        int intensity,
        @Size(min = 1, max = 255, message = "address is required length 1-255")
        @NotEmpty(message = "address is required")
        String address,

        @Size(min = 1, max = 255, message = "dateEnd is required length 1-255")
        @NotEmpty(message = "dateEnd is required")
        String dateEnd,
        String dateEndFormatted,

        @Size(min = 1, max = 255, message = "time is required length 1-255")
        @NotEmpty(message = "time is required")
        String time,

        @Size(min = 1, max = 5000, message = "description is required length 1-5000")
        @NotEmpty(message = "description is required")
        String description,

        String status,
        String statusName,

        Long userId,
        String userFio,
        String userImg,

        List<TaskReportDto> reports
) {
}
