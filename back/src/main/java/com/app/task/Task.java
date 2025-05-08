package com.app.task;

import com.app.appUser.AppUser;
import com.app.enums.TaskStatus;
import com.app.task_report.TaskReport;
import com.app.util.Global;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.app.util.Global.getDateNow;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Task implements Serializable {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "task_g")
    @SequenceGenerator(name = "task_g", sequenceName = "task_seq", allocationSize = 1)
    private Long id;

    private String name;
    private String date = getDateNow();
    private int intensity;
    private String address;
    private String dateEnd;
    private String time;

    @Column(length = 5000)
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.WAITING;

    @ManyToOne
    private AppUser user;
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<TaskReport> reports = new ArrayList<>();

    public Task(Long id, String name, int intensity, String address, String dateEnd, String time, String description) {
        this.id = id;
        this.name = name;
        this.intensity = intensity;
        this.address = address;
        this.dateEnd = dateEnd;
        this.time = time;
        this.description = description;
    }

    public Task(String name, int intensity, String address, String dateEnd, String time, String description) {
        this.name = name;
        this.intensity = intensity;
        this.address = address;
        this.dateEnd = dateEnd;
        this.time = time;
        this.description = description;
    }

    public void update(Task update) {
        this.name = update.getName();
        this.intensity = update.getIntensity();
        this.address = update.getAddress();
        this.dateEnd = update.getDateEnd();
        this.time = update.getTime();
        this.description = update.getDescription();
    }

    public String getDateFormatted() {
        return Global.getDateFormatted(date);
    }

    public String getDateEndFormatted() {
        return Global.getDateFormatted(dateEnd);
    }

    public List<TaskReport> getReports() {
        reports.sort(Comparator.comparing(TaskReport::getId));
        Collections.reverse(reports);
        return reports;
    }
}