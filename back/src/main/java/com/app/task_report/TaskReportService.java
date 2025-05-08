package com.app.task_report;

import com.app.system.exception.BadRequestException;
import com.app.system.exception.ObjectNotFoundException;
import com.app.task.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.app.util.Global.saveFile;

@Service
@RequiredArgsConstructor
public class TaskReportService {

    private final TaskReportRepository repository;
    private final TaskService taskService;

    public TaskReport find(String id) {
        return repository.findById(Long.parseLong(id)).orElseThrow(() -> new ObjectNotFoundException("Не найден отчет по ИД: " + id));
    }

    public TaskReport save(TaskReport save, String taskId) {
        save.setTask(taskService.find(taskId));
        return repository.save(save);
    }

    public TaskReport updateImg(String id, MultipartFile img) {
        TaskReport report = find(id);
        try {
            report.setImg(saveFile(img, "report"));
        } catch (IOException e) {
            if (report.getImg().isEmpty()) repository.deleteById(report.getId());
            throw new BadRequestException("Некорректное изображение");
        }
        return repository.save(report);
    }

    public TaskReport updateFile(String id, MultipartFile file) {
        TaskReport report = find(id);
        try {
            report.setFile(saveFile(file, "report"));
        } catch (IOException e) {
            if (report.getImg().isEmpty()) repository.deleteById(report.getId());
            throw new BadRequestException("Некорректный файл");
        }
        return repository.save(report);
    }

}
