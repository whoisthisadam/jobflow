package com.app.task;

import com.app.appUser.AppUser;
import com.app.appUser.UserService;
import com.app.enums.TaskStatus;
import com.app.system.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository repository;
    private final UserService userService;

    public List<Task> findAll() {
        List<Task> tasks = new ArrayList<>();

        AppUser user = userService.getCurrentUser();

        switch (user.getRole()) {
            case USER -> tasks = user.getTasks();
            case MANAGER -> tasks = repository.findAll();
        }

        tasks.sort(Comparator.comparing(Task::getId));
        Collections.reverse(tasks);

        return tasks;
    }

    public List<Task> findAllForTest() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public Task find(String id) {
        return repository.findById(Long.parseLong(id)).orElseThrow(() -> new ObjectNotFoundException("Не найдена задача по ИД: " + id));
    }

    public Task save(Task save, String userId) {
        save.setUser(userService.find(userId));
        return repository.save(save);
    }

    public Task saveForTest(Task save) {
        return repository.save(save);
    }

    public Task updateForTest(String id, Task update) {
        Task old = find(id);
        old.update(update);
        return repository.save(old);
    }

    public Task work(String id) {
        Task task = find(id);
        task.setStatus(TaskStatus.WORK);
        return repository.save(task);
    }

    public Task done(String id) {
        Task task = find(id);
        task.setStatus(TaskStatus.DONE);
        return repository.save(task);
    }

    public void deleteForTest(String id) {
        repository.deleteById(find(id).getId());
    }

}
