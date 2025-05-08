package com.app.category;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record CategoryDto(
        Long id,

        @Size(min = 1, max = 255, message = "name is required length 1-255")
        @NotEmpty(message = "name is required")
        String name,
        @Min(value = 0, message = "sum is required min 0.01")
        @Max(value = 1000000, message = "sum is required min 1000000")
        float sum
) {
}
