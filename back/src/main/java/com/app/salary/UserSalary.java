package com.app.salary;

import com.app.appUser.AppUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

import static com.app.util.Global.round;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_salary")
public class UserSalary implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private Integer month;

    @Column(nullable = false)
    private Float baseSalary;

    @Column(nullable = false)
    private Float taskIntensityBonus;

    @Column(nullable = false)
    private Float experienceBonus;

    @Column(nullable = false)
    private Float incomeTax;

    @Column(nullable = false)
    private Float cppTax;

    @Column(nullable = false)
    private Float otherDeductions;

    @Column(nullable = false)
    private Float totalSalary;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private AppUser createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        calculateTotalSalary();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        calculateTotalSalary();
    }

    public void calculateTotalSalary() {
        // Base salary + bonuses - taxes and deductions
        totalSalary = round(baseSalary + taskIntensityBonus + experienceBonus - incomeTax - cppTax - otherDeductions);
    }
}
