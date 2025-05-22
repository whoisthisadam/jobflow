//package com.app.appUser;
//
//import com.app.category.Category;
//import com.app.category.CategoryService;
//import com.app.enums.Role;
//import com.app.enums.TaskStatus;
//import com.app.system.exception.BadRequestException;
//import com.app.system.exception.ObjectNotFoundException;
//import com.app.task.Task;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class UserServiceTest {
//
//    @Mock
//    private UserRepository repository;
//
//    @Mock
//    private PasswordEncoder passwordEncoder;
//
//    @Mock
//    private CategoryService categoryService;
//
//    @InjectMocks
//    private UserService service;
//
//    private List<AppUser> users = new ArrayList<>();
//    private Category category;
//
//    @BeforeEach
//    void setUp() {
//        // Create a test category
//        category = new Category(1L, "Test Category", 10.0f);
//
//        // Create test users with different roles
//        AppUser adminUser = new AppUser();
////        adminUser.setId(1L);
//        adminUser.setUsername("admin");
//        adminUser.setPassword("password");
//        adminUser.setRole(Role.ADMIN);
//        adminUser.setFio("Admin User");
//        adminUser.setExp(5);
//        adminUser.setCategory(category);
//
//        AppUser managerUser = new AppUser();
////        managerUser.setId(2L);
//        managerUser.setUsername("manager");
//        managerUser.setPassword("password");
//        managerUser.setRole(Role.MANAGER);
//        managerUser.setFio("Manager User");
//        managerUser.setExp(3);
//
//        AppUser regularUser = new AppUser();
////        regularUser.setId(3L);
//        regularUser.setUsername("user");
//        regularUser.setPassword("password");
//        regularUser.setRole(Role.USER);
//        regularUser.setFio("Regular User");
//        regularUser.setExp(1);
//
//        users.add(adminUser);
//        users.add(managerUser);
//        users.add(regularUser);
//    }
//
//    @AfterEach
//    void tearDown() {
//        users.clear();
//    }
//
//    @Test
//    void testLoadUserByUsernameSuccess() {
//        AppUser user = users.get(0);
//
//        given(repository.findByUsername("admin")).willReturn(Optional.of(user));
//
//        MyUserPrincipal userDetails = (MyUserPrincipal) service.loadUserByUsername("admin");
//
//        assertThat(userDetails.getUsername()).isEqualTo(user.getUsername());
//        assertThat(userDetails.getPassword()).isEqualTo(user.getPassword());
//
//        verify(repository, times(1)).findByUsername("admin");
//    }
//
//    @Test
//    void testLoadUserByUsernameNotFound() {
//        given(repository.findByUsername("nonexistent")).willReturn(Optional.empty());
//
//        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("nonexistent"));
//
//        verify(repository, times(1)).findByUsername("nonexistent");
//    }
//
//    @Test
//    void testFindAllSuccess() {
//        given(repository.findAll()).willReturn(users);
//
//        List<AppUser> result = service.findAll();
//
//        assertThat(result.size()).isEqualTo(users.size());
//        assertThat(result).isEqualTo(users);
//
//        verify(repository, times(1)).findAll();
//    }
//
//    @Test
//    void testFindAllByRoleUserSuccess() {
//        List<AppUser> userRoleUsers = users.stream()
//                .filter(user -> user.getRole() == Role.USER)
//                .toList();
//
//        given(repository.findAllByRole(Role.USER)).willReturn(userRoleUsers);
//
//        List<AppUser> result = service.findAllByRoleUser();
//
//        assertThat(result.size()).isEqualTo(1);
//        assertThat(result.get(0).getUsername()).isEqualTo("user");
//
//        verify(repository, times(1)).findAllByRole(Role.USER);
//    }
//
//    @Test
//    void testFindSuccess() {
//        AppUser user = users.get(0);
//
//        given(repository.findById(1L)).willReturn(Optional.of(user));
//
//        AppUser result = service.find("1");
//
//        assertThat(result).isEqualTo(user);
//        assertThat(result.getId()).isEqualTo(1L);
//        assertThat(result.getUsername()).isEqualTo("admin");
//
//        verify(repository, times(1)).findById(1L);
//    }
//
//    @Test
//    void testFindNotFound() {
//        given(repository.findById(Mockito.any(Long.class))).willReturn(Optional.empty());
//
//        assertThrows(ObjectNotFoundException.class, () -> service.find("1"));
//
//        verify(repository, times(1)).findById(1L);
//    }
//
//    @Test
//    void testFindInvalidId() {
//        assertThrows(ObjectNotFoundException.class, () -> service.find("invalid"));
//
//        verify(repository, never()).findById(Mockito.any());
//    }
//
//    @Test
//    void testSaveSuccess() {
//        AppUser newUser = new AppUser();
//        newUser.setUsername("newuser");
//        newUser.setPassword("password");
//
//        given(repository.findByUsername("newuser")).willReturn(Optional.empty());
//        given(passwordEncoder.encode("password")).willReturn("encodedPassword");
//        given(repository.findAll()).willReturn(users);
//        given(repository.save(newUser)).willReturn(newUser);
//
//        AppUser result = service.save(newUser);
//
//        assertThat(result).isEqualTo(newUser);
//        assertThat(result.getPassword()).isEqualTo("encodedPassword");
//        assertThat(result.getRole()).isEqualTo(Role.USER); // Should be USER since users list is not empty
//
//        verify(repository, times(1)).findByUsername("newuser");
//        verify(passwordEncoder, times(1)).encode("password");
//        verify(repository, times(1)).findAll();
//        verify(repository, times(1)).save(newUser);
//    }
//
//    @Test
//    void testSaveFirstUserAsAdmin() {
//        AppUser newUser = new AppUser();
//        newUser.setUsername("newuser");
//        newUser.setPassword("password");
//
//        given(repository.findByUsername("newuser")).willReturn(Optional.empty());
//        given(passwordEncoder.encode("password")).willReturn("encodedPassword");
//        given(repository.findAll()).willReturn(new ArrayList<>()); // Empty list
//        given(repository.save(newUser)).willReturn(newUser);
//
//        AppUser result = service.save(newUser);
//
//        assertThat(result).isEqualTo(newUser);
//        assertThat(result.getPassword()).isEqualTo("encodedPassword");
//        assertThat(result.getRole()).isEqualTo(Role.ADMIN); // Should be ADMIN since users list is empty
//
//        verify(repository, times(1)).findByUsername("newuser");
//        verify(passwordEncoder, times(1)).encode("password");
//        verify(repository, times(1)).findAll();
//        verify(repository, times(1)).save(newUser);
//    }
//
//    @Test
//    void testSaveUsernameTaken() {
//        AppUser newUser = new AppUser();
//        newUser.setUsername("admin"); // Already exists
//        newUser.setPassword("password");
//
//        given(repository.findByUsername("admin")).willReturn(Optional.of(users.get(0)));
//
//        assertThrows(BadRequestException.class, () -> service.save(newUser));
//
//        verify(repository, times(1)).findByUsername("admin");
//        verify(repository, never()).save(Mockito.any());
//    }
//
//    @Test
//    void testUpdateRoleSuccess() {
//        AppUser user = users.get(2); // Regular user
//
//        given(repository.findById(3L)).willReturn(Optional.of(user));
//        given(repository.save(user)).willReturn(user);
//
//        AppUser result = service.updateRole("3", "MANAGER");
//
//        assertThat(result).isEqualTo(user);
//        assertThat(result.getRole()).isEqualTo(Role.MANAGER);
//
//        verify(repository, times(1)).findById(3L);
//        verify(repository, times(1)).save(user);
//    }
//
//    @Test
//    void testUpdateRoleInvalidRole() {
//        AppUser user = users.get(2); // Regular user
//
//        given(repository.findById(3L)).willReturn(Optional.of(user));
//
//        assertThrows(BadRequestException.class, () -> service.updateRole("3", "INVALID_ROLE"));
//
//        verify(repository, times(1)).findById(3L);
//        verify(repository, never()).save(Mockito.any());
//    }
//
//    @Test
//    void testUpdateRoleUserNotFound() {
//        given(repository.findById(99L)).willReturn(Optional.empty());
//
//        assertThrows(ObjectNotFoundException.class, () -> service.updateRole("99", "ADMIN"));
//
//        verify(repository, times(1)).findById(99L);
//        verify(repository, never()).save(Mockito.any());
//    }
//
//    @Test
//    void testUpdateCategorySuccess() {
//        AppUser user = users.get(2); // Regular user
//        Category newCategory = new Category(2L, "New Category", 15.0f);
//
//        given(repository.findById(3L)).willReturn(Optional.of(user));
//        given(categoryService.find("2")).willReturn(newCategory);
//        given(repository.save(user)).willReturn(user);
//
//        AppUser result = service.updateCategory("3", "2");
//
//        assertThat(result).isEqualTo(user);
//        assertThat(result.getCategory()).isEqualTo(newCategory);
//
//        verify(repository, times(1)).findById(3L);
//        verify(categoryService, times(1)).find("2");
//        verify(repository, times(1)).save(user);
//    }
//
//    @Test
//    void testUpdateExpSuccess() {
//        AppUser user = users.get(2); // Regular user
//
//        given(repository.findById(3L)).willReturn(Optional.of(user));
//        given(repository.save(user)).willReturn(user);
//
//        AppUser result = service.updateExp("3", 10);
//
//        assertThat(result).isEqualTo(user);
//        assertThat(result.getExp()).isEqualTo(10);
//
//        verify(repository, times(1)).findById(3L);
//        verify(repository, times(1)).save(user);
//    }
//
//    @Test
//    void testDeleteByIdSuccess() {
//        AppUser user = users.get(2); // Regular user
//
//        given(repository.findById(3L)).willReturn(Optional.of(user));
//        doNothing().when(repository).deleteById(3L);
//
//        service.deleteById("3");
//
//        verify(repository, times(1)).findById(3L);
//        verify(repository, times(1)).deleteById(3L);
//    }
//
//    @Test
//    void testDeleteByIdUserNotFound() {
//        given(repository.findById(99L)).willReturn(Optional.empty());
//
//        assertThrows(ObjectNotFoundException.class, () -> service.deleteById("99"));
//
//        verify(repository, times(1)).findById(99L);
//        verify(repository, never()).deleteById(Mockito.any());
//    }
//
//    @Test
//    void testFindIncomeWithEmptyDate() {
//        // Create a user with a category and tasks
//        AppUser user = new AppUser();
//        user.setUsername("testuser");
//        user.setCategory(category); // Category has sum = 10.0f
//        user.setExp(5); // This gives a ratio of 1.5f
//
//        // Add some tasks with different statuses
//        List<Task> tasks = new ArrayList<>();
//
//        // Done tasks (should be counted)
//        Task doneTask1 = new Task();
//        doneTask1.setStatus(TaskStatus.DONE);
//        doneTask1.setIntensity(2);
//        doneTask1.setDate("2023-05-15");
//
//        Task doneTask2 = new Task();
//        doneTask2.setStatus(TaskStatus.DONE);
//        doneTask2.setIntensity(3);
//        doneTask2.setDate("2023-06-20");
//
//        // Work task (should not be counted)
//        Task workTask = new Task();
//        workTask.setStatus(TaskStatus.WORK);
//        workTask.setIntensity(4);
//        workTask.setDate("2023-07-10");
//
//        // Waiting task (should not be counted)
//        Task waitingTask = new Task();
//        waitingTask.setStatus(TaskStatus.WAITING);
//        waitingTask.setIntensity(5);
//        waitingTask.setDate("2023-08-05");
//
//        tasks.add(doneTask1);
//        tasks.add(doneTask2);
//        tasks.add(workTask);
//        tasks.add(waitingTask);
//
//        user.setTasks(tasks);
//
//        // Mock getCurrentUser to return our test user
//        doReturn(user).when(service).getCurrentUser();
//
//        // Call the method with empty date
//        float income = service.findIncome("");
//
//        // Expected calculation: 10.0f * 0.86f * 1.5f * (2 + 3) = 10 * 0.86 * 1.5 * 5 = 64.5
//        // With rounding, it should be 64.5
//        assertThat(income).isEqualTo(64.5f);
//
//        // Verify getCurrentUser was called
//        verify(service, times(1)).getCurrentUser();
//    }
//
//    @Test
//    void testFindIncomeWithSpecificDate() {
//        // Create a user with a category and tasks
//        AppUser user = new AppUser();
//        user.setUsername("testuser");
//        user.setCategory(category); // Category has sum = 10.0f
//        user.setExp(2); // This gives a ratio of 1.2f
//
//        // Add some tasks with different statuses and dates
//        List<Task> tasks = new ArrayList<>();
//
//        // Done tasks with matching date (should be counted)
//        Task doneTask1 = new Task();
//        doneTask1.setStatus(TaskStatus.DONE);
//        doneTask1.setIntensity(2);
//        doneTask1.setDate("2023-05-15");
//
//        // Done tasks with non-matching date (should not be counted)
//        Task doneTask2 = new Task();
//        doneTask2.setStatus(TaskStatus.DONE);
//        doneTask2.setIntensity(3);
//        doneTask2.setDate("2023-06-20");
//
//        // Done tasks with matching date (should be counted)
//        Task doneTask3 = new Task();
//        doneTask3.setStatus(TaskStatus.DONE);
//        doneTask3.setIntensity(4);
//        doneTask3.setDate("2023-05-25");
//
//        // Work task with matching date (should not be counted due to status)
//        Task workTask = new Task();
//        workTask.setStatus(TaskStatus.WORK);
//        workTask.setIntensity(5);
//        workTask.setDate("2023-05-10");
//
//        tasks.add(doneTask1);
//        tasks.add(doneTask2);
//        tasks.add(doneTask3);
//        tasks.add(workTask);
//
//        user.setTasks(tasks);
//
//        // Mock getCurrentUser to return our test user
//        doReturn(user).when(service).getCurrentUser();
//
//        // Call the method with specific date (2023-05)
//        float income = service.findIncome("2023-05");
//
//        // Expected calculation: 10.0f * 0.86f * 1.2f * (2 + 4) = 10 * 0.86 * 1.2 * 6 = 61.92
//        // With rounding, it should be 61.92
//        assertThat(income).isEqualTo(61.92f);
//
//        // Verify getCurrentUser was called
//        verify(service, times(1)).getCurrentUser();
//    }
//
//    @Test
//    void testFindIncomeWithNoMatchingTasks() {
//        // Create a user with a category and tasks
//        AppUser user = new AppUser();
//        user.setUsername("testuser");
//        user.setCategory(category); // Category has sum = 10.0f
//        user.setExp(1); // This gives a ratio of 1.0f
//
//        // Add some tasks with different statuses and dates
//        List<Task> tasks = new ArrayList<>();
//
//        // Done tasks with non-matching date
//        Task doneTask1 = new Task();
//        doneTask1.setStatus(TaskStatus.DONE);
//        doneTask1.setIntensity(2);
//        doneTask1.setDate("2023-06-15");
//
//        // Work tasks with matching date (should not be counted due to status)
//        Task workTask = new Task();
//        workTask.setStatus(TaskStatus.WORK);
//        workTask.setIntensity(3);
//        workTask.setDate("2023-05-10");
//
//        tasks.add(doneTask1);
//        tasks.add(workTask);
//
//        user.setTasks(tasks);
//
//        // Mock getCurrentUser to return our test user
//        doReturn(user).when(service).getCurrentUser();
//
//        // Call the method with specific date (2023-05)
//        float income = service.findIncome("2023-05");
//
//        // Expected calculation: 10.0f * 0.86f * 1.0f * 0 = 0
//        assertThat(income).isEqualTo(0.0f);
//
//        // Verify getCurrentUser was called
//        verify(service, times(1)).getCurrentUser();
//    }
//
//    @Test
//    void testFindIncomeWithZeroExperience() {
//        // Create a user with a category and tasks
//        AppUser user = new AppUser();
//        user.setUsername("testuser");
//        user.setCategory(category); // Category has sum = 10.0f
//        user.setExp(0); // This gives a ratio of 0.0f
//
//        // Add some done tasks
//        List<Task> tasks = new ArrayList<>();
//
//        Task doneTask = new Task();
//        doneTask.setStatus(TaskStatus.DONE);
//        doneTask.setIntensity(5);
//        doneTask.setDate("2023-05-15");
//
//        tasks.add(doneTask);
//
//        user.setTasks(tasks);
//
//        // Mock getCurrentUser to return our test user
//        doReturn(user).when(service).getCurrentUser();
//
//        // Call the method with empty date
//        float income = service.findIncome("");
//
//        // Expected calculation: 10.0f * 0.86f * 0.0f * 5 = 0
//        assertThat(income).isEqualTo(0.0f);
//
//        // Verify getCurrentUser was called
//        verify(service, times(1)).getCurrentUser();
//    }
//}
