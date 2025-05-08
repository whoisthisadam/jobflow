package com.app.task_report;

import com.app.task.Task;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

import static com.app.util.Global.getDateAndTimeNow;
import static com.app.util.Global.getDateTimeFormatted;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class TaskReport implements Serializable {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "task_report_g")
    @SequenceGenerator(name = "task_report_g", sequenceName = "task_report_seq", allocationSize = 1)
    private Long id;

    private String name;
    private String date = getDateAndTimeNow();

    @Column(length = 1000)
    private String img = "";
    @Column(length = 1000)
    private String file = "";

    @ManyToOne
    private Task task;

    public TaskReport(String name) {
        this.name = name;
    }

    public String getDateFormatted() {
        return getDateTimeFormatted(date);
    }
}