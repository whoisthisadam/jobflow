import { Component, OnInit } from '@angular/core';
import { SalaryService } from './salary.service';
import { UserService } from '../user/user.service';
import { FormsModule } from "@angular/forms";
import { AlertService } from "../alert/alert.service";
import { NgIf, NgFor } from "@angular/common";
import { DatePipe } from "@angular/common";

@Component({
	selector: 'app-salary-management',
	standalone: true,
	imports: [
		FormsModule,
		NgIf,
		NgFor,
		DatePipe
	],
	templateUrl: './salary-management.component.html'
})
export class SalaryManagementComponent implements OnInit {

  users: any[] = [];
  selectedUser: any = null;
  year: number = new Date().getFullYear();
  month: number = new Date().getMonth() + 1;

  salary: any = {
    userId: null,
    year: this.year,
    month: this.month,
    baseSalary: 0,
    taskIntensityBonus: 0,
    experienceBonus: 0,
    incomeTax: 0,
    cppTax: 0,
    otherDeductions: 0,
    totalSalary: 0
  };

  existingSalary: any = null;
  isEditMode: boolean = false;

  constructor(
    private salaryService: SalaryService,
    private userService: UserService,
    private alertService: AlertService
  ) { }

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers() {
    this.userService.findAllByRoleUser();
    this.userService.userSubject.subscribe(data => {
      this.users = data.roleUser;
    });
  }

  onUserSelect(event: any) {
    const userId = event.target.value;
    if (userId) {
      this.selectedUser = this.users.find(user => user.id == userId);
      this.salary.userId = userId;

      // Get detailed user information if needed
      if (this.selectedUser && !this.selectedUser.tasksIntensity) {
        this.userService.find(userId).subscribe({
          next: (res: any) => {
            if (res.flag && res.data) {
              // Update user details with more information
              this.selectedUser = res.data;
            }
            this.checkExistingSalary();
          },
          error: () => {
            this.checkExistingSalary();
          }
        });
      } else {
        this.checkExistingSalary();
      }
    } else {
      this.selectedUser = null;
      this.salary.userId = null;
      this.existingSalary = null;
      this.isEditMode = false;
    }
  }

  onYearMonthChange() {
    this.salary.year = this.year;
    this.salary.month = this.month;
    if (this.selectedUser) {
      this.checkExistingSalary();
    }
  }

  checkExistingSalary() {
    this.salaryService.findByUserAndYearAndMonth(this.salary.userId, this.year, this.month)
      .subscribe({
        next: (res: any) => {
          if (res.flag) {
            this.existingSalary = res.data;
            this.alertService.showAlertMessage('Запись о зарплате уже существует. Вы можете отредактировать её.');
          } else {
            this.existingSalary = null;
            this.resetSalaryForm();
          }
        },
        error: () => {
          this.existingSalary = null;
          this.resetSalaryForm();
        }
      });
  }

  resetSalaryForm() {
    this.isEditMode = false;

    // Get base salary from user's category if available
    let baseSalary = 0;
    if (this.selectedUser && this.selectedUser.categorySum) {
      baseSalary = this.selectedUser.categorySum;
    }

    // Calculate default experience bonus based on user's experience
    let experienceBonus = 0;
    if (this.selectedUser && this.selectedUser.exp) {
      // Apply the same ratio logic as in the backend
      let ratio = 0;
      const exp = this.selectedUser.exp;
      if (exp === 1) ratio = 1;
      else if (exp >= 2 && exp <= 4) ratio = 1.2;
      else if (exp >= 5 && exp <= 8) ratio = 1.5;
      else if (exp > 8) ratio = 1.8;

      // Calculate experience bonus as a percentage of base salary
      experienceBonus = Math.round(baseSalary * (ratio - 1) * 100) / 100;
    }

    // Calculate task intensity bonus based on user's completed tasks
    let taskIntensityBonus = 0;
    if (this.selectedUser && this.selectedUser.tasksIntensity && baseSalary > 0) {
      // Simple formula: 10% of base salary per 10 hours of task intensity
      const hourlyRate = baseSalary / 160; // Assuming 160 working hours per month
      taskIntensityBonus = Math.round(hourlyRate * this.selectedUser.tasksIntensity * 100) / 100;
    }

    // Calculate default income tax (13%)
    const incomeTax = Math.round((baseSalary + experienceBonus + taskIntensityBonus) * 0.13 * 100) / 100;

    // Calculate default CPP tax (2%)
    const cppTax = Math.round((baseSalary + experienceBonus + taskIntensityBonus) * 0.01 * 100) / 100;

    this.salary = {
      userId: this.selectedUser ? this.selectedUser.id : null,
      year: this.year,
      month: this.month,
      baseSalary: baseSalary,
      taskIntensityBonus: taskIntensityBonus,
      experienceBonus: experienceBonus,
      incomeTax: incomeTax,
      cppTax: cppTax,
      otherDeductions: 0,
      totalSalary: 0
    };

    // Calculate initial total salary
    this.calculateTotalSalary();
  }

  editExistingSalary() {
    this.isEditMode = true;
    this.salary = {
      id: this.existingSalary.id,
      userId: this.existingSalary.userId,
      year: this.existingSalary.year,
      month: this.existingSalary.month,
      baseSalary: this.existingSalary.baseSalary,
      taskIntensityBonus: this.existingSalary.taskIntensityBonus,
      experienceBonus: this.existingSalary.experienceBonus,
      incomeTax: this.existingSalary.incomeTax,
      cppTax: this.existingSalary.cppTax,
      otherDeductions: this.existingSalary.otherDeductions,
      totalSalary: this.existingSalary.totalSalary
    };
  }

  calculateTotalSalary() {
    this.salary.totalSalary =
      (this.salary.baseSalary + this.salary.taskIntensityBonus + this.salary.experienceBonus) -
      (this.salary.incomeTax + this.salary.cppTax + this.salary.otherDeductions);

    // Round to 2 decimal places
    this.salary.totalSalary = Math.round(this.salary.totalSalary * 100) / 100;
  }

  saveSalary() {
    this.calculateTotalSalary();

    if (!this.salary.userId) {
      this.alertService.showAlertMessage('Пожалуйста, выберите пользователя');
      return;
    }

    if (this.isEditMode) {
      this.salaryService.update(this.salary).subscribe({
        next: (res: any) => {
          if (res.flag) {
            this.alertService.showAlertMessage('Запись о зарплате успешно обновлена');
            this.existingSalary = res.data;
            this.isEditMode = false;
          } else {
            this.alertService.showAlertMessage('Ошибка при обновлении записи о зарплате');
          }
        },
        error: () => {
          this.alertService.showAlertMessage('Ошибка при обновлении записи о зарплате');
        }
      });
    } else {
      this.salaryService.save(this.salary).subscribe({
        next: (res: any) => {
          if (res.flag) {
            this.alertService.showAlertMessage('Запись о зарплате успешно создана');
            this.existingSalary = res.data;
          } else {
            this.alertService.showAlertMessage('Ошибка при создании записи о зарплате');
          }
        },
        error: () => {
          this.alertService.showAlertMessage('Ошибка при создании записи о зарплате');
        }
      });
    }
  }

  cancelEdit() {
    this.isEditMode = false;
    this.resetSalaryForm();
  }
}
