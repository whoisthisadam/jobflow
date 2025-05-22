import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {GlobalService} from "../global.service";
import {AccountService} from "./account.service";
import {AlertService} from "../alert/alert.service";
import {FormsModule} from "@angular/forms";
import {NgIf} from "@angular/common";
import {SalaryService} from "../salary/salary.service";

@Component({
	selector: 'app-account',
	imports: [
		FormsModule,
		NgIf
	],
	templateUrl: './account.component.html',
	standalone: true
})

export class AccountComponent implements OnInit {

	account: any;

	year: number = new Date().getFullYear();
	month: number = new Date().getMonth() + 1;

	income: any;
	salaryData: any = null;
	isSalarySet: boolean = false;

	constructor(
		private router: Router,
		private global: GlobalService,
		private accountService: AccountService,
		private alert: AlertService,
		private salaryService: SalaryService,
	) {
	}

	get role() {
		return this.global.role;
	}

	ngOnInit(): void {
		if (this.role === 'NOT') this.router.navigate(['/login']);

		this.accountService.find().subscribe({
			next: (res: any) => {
				this.account = res.data;
				this.accountService.findIncome("").subscribe({
					next: (res: any) => {
						this.income = res.data
					},
					error: (e: any) => this.global.error(e),
				})
			},
			error: (e: any) => {
				console.error();
				if (e.error.code === 404) {
					this.router.navigate(['/error'], {queryParams: {message: e.error.message}});
				} else {
					this.router.navigate(['/login']);
				}
			}
		})
	}

	updateImg(event: any) {
		this.accountService.updateImg(event.target.files).subscribe({
			next: (res: any) => this.account = res.data,
			error: (e: any) => this.global.error(e),
		})
	}

	updateFio() {
		this.accountService.updateFio(this.account.fio, this.account.exp).subscribe({
			next: (res: any) => {
				this.alert.showAlertMessage("Данные обновлены");
				this.account = res.data
				this.findIncome();
			},
			error: (e: any) => this.global.error(e),
		})
	}

	findIncome() {
		// Reset salary data
		this.salaryData = null;
		this.isSalarySet = false;

		if (this.year != 0 && this.month >= 1 && this.month <= 12) {
			let date: string = this.year.toString() + '-';

			if (this.month >= 1 && this.month <= 9) {
				date += "0" + this.month.toString()
			} else if (this.month >= 10 && this.month <= 12) {
				date += this.month.toString()
			}

			// First try to get salary data from the new system
			this.salaryService.findByCurrentUserAndYearAndMonth(this.year, this.month).subscribe({
				next: (res: any) => {
					if (res.flag) {
						// Salary data found in the new system
						this.salaryData = res.data;
						this.income = this.salaryData.totalSalary;
						this.isSalarySet = true;
					} else {
						// No salary data found, fall back to the old system
						this.accountService.findIncome(date).subscribe({
							next: (res: any) => {
								if (typeof res.data === 'string') {
									// This is a message indicating salary is not set
									this.income = 0;
									this.isSalarySet = false;
								} else {
									// This is the calculated income from the old system
									this.income = res.data;
									this.isSalarySet = true;
								}
							},
							error: (e: any) => this.global.error(e),
						});
					}
				},
				error: () => {
					// Error getting salary data, fall back to the old system
					this.accountService.findIncome(date).subscribe({
						next: (res: any) => {
							if (typeof res.data === 'string') {
								// This is a message indicating salary is not set
								this.income = 0;
								this.isSalarySet = false;
							} else {
								// This is the calculated income from the old system
								this.income = res.data;
								this.isSalarySet = true;
							}
						},
						error: (e: any) => this.global.error(e),
					});
				},
			});
		} else {
			// If no year/month selected, get total income
			this.accountService.findIncome("").subscribe({
				next: (res: any) => {
					this.income = res.data;
					this.isSalarySet = true;
				},
				error: (e: any) => this.global.error(e),
			});
		}
	}

}
