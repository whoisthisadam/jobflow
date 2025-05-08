package com.app.task.converter;

import com.app.task.Task;
import com.app.task.TaskDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TaskDtoToTaskConverter implements Converter<TaskDto, Task> {
    @Override
    public Task convert(TaskDto source) {
        return new Task(
                source.name(),
                source.intensity(),
                source.address(),
                source.dateEnd(),
                source.time(),
                source.description()
        );
    }
}
