package com.app.task_report;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record TaskReportDto(
        Long id,

        @Size(min = 1, max = 255, message = "name is required length 1-255")
        @NotEmpty(message = "name is required")
        String name,

        String date,
        String dateFormatted,

        String img,
        String file
) {
}
