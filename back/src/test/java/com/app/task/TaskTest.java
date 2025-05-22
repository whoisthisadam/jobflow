package com.app.task;

import com.app.enums.TaskStatus;
import com.app.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskTest {

    @Mock
    TaskRepository repository;
    @InjectMocks
    TaskService service;

    List<Task> tasks = new ArrayList<>();

    @BeforeEach
    void setUp() {
        tasks.add(new Task(1L, "name1", 1, "address1", "dateEnd1", "time1", "description1"));
        tasks.add(new Task(2L, "name2", 2, "address2", "dateEnd2", "time2", "description2"));
        tasks.add(new Task(3L, "name3", 3, "address3", "dateEnd3", "time3", "description3"));
        tasks.add(new Task(3L, "name3", 3, "address3", "dateEnd3", "time3", "description4"));
    }

    @AfterEach
    void tearDown() {
        tasks.clear();
    }

    @Test
    void testFindAllSuccess() {
        given(service.findAllForTest()).willReturn(tasks);

        List<Task> findAll = service.findAllForTest();

        assertThat(findAll.size()).isEqualTo(tasks.size());

        verify(repository, times(1)).findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Test
    void testFindSuccess() {
        Task task = tasks.get(0);

        given(repository.findById(1L)).willReturn(Optional.of(task));

        Task find = service.find("1");

        assertThat(find.getId()).isEqualTo(task.getId());
        assertThat(find.getName()).isEqualTo(task.getName());

        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testFindNotFound() {
        given(repository.findById(Mockito.any(Long.class))).willReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> service.find("1"));

        verify(repository, Mockito.times(1)).findById(1L);
    }

    @Test
    void testSaveSuccess() {
        Task save = tasks.get(0);

        given(repository.save(save)).willReturn(save);

        Task saved = service.saveForTest(save);

        assertThat(saved.getId()).isEqualTo(1L);
        assertThat(saved.getName()).isEqualTo(save.getName());

        verify(repository, times(1)).save(save);
    }

    @Test
    void testUpdateSuccess() {
        Task old = tasks.get(0);

        Task update = new Task("name update", 5, "address update", "dateEnd update", "time update", "description update");

        given(repository.findById(1L)).willReturn(Optional.of(old));
        given(repository.save(old)).willReturn(old);

        Task updated = service.updateForTest(old.getId() + "", update);

        assertThat(updated.getId()).isEqualTo(1L);
        assertThat(updated.getName()).isEqualTo(update.getName());

        verify(repository, times(1)).findById(old.getId());
        verify(repository, times(1)).save(old);
    }

    @Test
    void testUpdateNotFound() {
        Task update = tasks.get(0);

        given(repository.findById(1L)).willReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> service.updateForTest("1", update));

        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testDeleteSuccess() {
        Task delete = tasks.get(0);

        given(repository.findById(1L)).willReturn(Optional.of(delete));
        doNothing().when(repository).deleteById(1L);

        service.deleteForTest("1");

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteNotFound() {
        given(repository.findById(1L)).willReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> service.deleteForTest("1"));

        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testWorkSuccess() {
        Task task = tasks.get(0);
        task.setStatus(TaskStatus.WAITING); // Ensure initial status is WAITING

        given(repository.findById(1L)).willReturn(Optional.of(task));
        given(repository.save(task)).willReturn(task);

        Task result = service.work("1");

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getStatus()).isEqualTo(TaskStatus.WORK);

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(task);
    }

    @Test
    void testWorkNotFound() {
        given(repository.findById(1L)).willReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> service.work("1"));

        verify(repository, times(1)).findById(1L);
        verify(repository, never()).save(any());
    }

    @Test
    void testDoneSuccess() {
        Task task = tasks.get(0);
        task.setStatus(TaskStatus.WORK); // Ensure initial status is WORK

        given(repository.findById(1L)).willReturn(Optional.of(task));
        given(repository.save(task)).willReturn(task);

        Task result = service.done("1");

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getStatus()).isEqualTo(TaskStatus.DONE);

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(task);
    }

    @Test
    void testDoneNotFound() {
        given(repository.findById(1L)).willReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> service.done("1"));

        verify(repository, times(1)).findById(1L);
        verify(repository, never()).save(any());
    }
}
