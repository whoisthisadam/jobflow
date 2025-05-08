package com.app.appUser;

import com.app.category.Category;
import com.app.enums.Role;
import com.app.enums.TaskStatus;
import com.app.task.Task;
import com.app.util.Global;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.app.util.Global.getDateNow;
import static com.app.util.Global.round;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class AppUser implements Serializable {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "app_user_g")
    @SequenceGenerator(name = "app_user_g", sequenceName = "app_user_seq", allocationSize = 1)
    private Long id;

    @Size(min = 1, max = 255, message = "username is required length 1-255")
    @NotEmpty(message = "username is required")
    private String username;
    @Size(min = 1, max = 255, message = "password is required length 1-255")
    @NotEmpty(message = "password is required")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    private String fio = "ФИО";
    private String date = getDateNow();
    private int exp = 0;

    @Column(length = 1000)
    private String img = "/img/avatar.png";

    @ManyToOne
    private Category category = null;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Task> tasks = new ArrayList<>();

    public AppUser(String username) {
        this.username = username;
    }

    public void set(AppUser user) {

    }

    public String getDateFormatted() {
        return Global.getDateFormatted(date);
    }

    public Long getCategoryId() {
        return category == null ? 0L : category.getId();
    }

    public String getCategoryName() {
        return category == null ? "" : category.getName();
    }

    public float getCategorySum() {
        return category == null ? 0f : category.getSum();
    }

    public int getTasksCount() {
        return tasks.size();
    }

    public int getTasksIntensity() {
        return tasks.stream().reduce(0, (i, task) -> i + task.getIntensity(), Integer::sum);
    }

    public float getIncome() {
        return round(getCategorySum() * 0.86f * getRatio() * tasks.stream().reduce(0, (i, task) -> {
            if (task.getStatus() == TaskStatus.DONE) return i + task.getIntensity();
            return i;
        }, Integer::sum));
    }

    public float getIncome(String date) {
        return round(getCategorySum() * 0.86f * getRatio() * tasks.stream().reduce(0, (i, task) -> {
            if (task.getStatus() == TaskStatus.DONE && task.getDate().contains(date)) return i + task.getIntensity();
            return i;
        }, Integer::sum));
    }

    public float getRatio() {
        if (exp == 0) return 0;
        if (exp == 1) return 1;
        if (exp >= 2 && exp <= 4) return 1.2f;
        if (exp >= 5 && exp <= 8) return 1.5f;
        if (exp > 8) return 1.8f;
        return 0;
    }

}