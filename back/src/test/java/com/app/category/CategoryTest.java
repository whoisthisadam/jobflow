package com.app.category;

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
public class CategoryTest {

    @Mock
    CategoryRepository repository;
    @InjectMocks
    CategoryService service;

    List<Category> categories = new ArrayList<>();

    @BeforeEach
    void setUp() {
        categories.add(new Category(1L, "name1", 1.99f));
        categories.add(new Category(2L, "name2", 2.99f));
        categories.add(new Category(3L, "name3", 3.99f));
        categories.add(new Category(4L, "name4", 4.99f));
    }

    @AfterEach
    void tearDown() {
        categories.clear();
    }

    @Test
    void testFindAllSuccess() {
        given(service.findAll()).willReturn(categories);

        List<Category> findAll = service.findAll();

        assertThat(findAll.size()).isEqualTo(categories.size());

        verify(repository, times(1)).findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Test
    void testFindSuccess() {
        Category category = categories.get(0);

        given(repository.findById(1L)).willReturn(Optional.of(category));

        Category find = service.find("1");

        assertThat(find.getId()).isEqualTo(category.getId());
        assertThat(find.getName()).isEqualTo(category.getName());

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
        Category save = categories.get(0);

        given(repository.save(save)).willReturn(save);

        Category saved = service.save(save);

        assertThat(saved.getId()).isEqualTo(1L);
        assertThat(saved.getName()).isEqualTo(save.getName());

        verify(repository, times(1)).save(save);
    }

    @Test
    void testUpdateSuccess() {
        Category old = categories.get(0);

        Category update = new Category("update name", 5.99f);

        given(repository.findById(1L)).willReturn(Optional.of(old));
        given(repository.save(old)).willReturn(old);

        Category updated = service.update(old.getId() + "", update);

        assertThat(updated.getId()).isEqualTo(1L);
        assertThat(updated.getName()).isEqualTo(update.getName());

        verify(repository, times(1)).findById(old.getId());
        verify(repository, times(1)).save(old);
    }

    @Test
    void testUpdateNotFound() {
        Category update = categories.get(0);

        given(repository.findById(1L)).willReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> service.update("1", update));

        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testDeleteSuccess() {
        Category delete = categories.get(0);

        given(repository.findById(1L)).willReturn(Optional.of(delete));
        doNothing().when(repository).deleteById(1L);

        service.delete("1");

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteNotFound() {
        given(repository.findById(1L)).willReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> service.delete("1"));

        verify(repository, times(1)).findById(1L);
    }

}
