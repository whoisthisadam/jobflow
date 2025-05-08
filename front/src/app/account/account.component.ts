import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {GlobalService} from "../global.service";
import {AccountService} from "./account.service";
import {AlertService} from "../alert/alert.service";
import {FormsModule} from "@angular/forms";
import {NgIf} from "@angular/common";

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

	year: number = 0;
	month: number = 0;

	income:any;

	constructor(
		private router: Router,
		private global: GlobalService,
		private accountService: AccountService,
		private alert: AlertService,
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
				console.error(e);
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
		if (this.year != 0) {
			let date: string = this.year.toString() + '-';

			if (this.month >= 1 && this.month <= 9) {
				date += "0" + this.month.toString()
			} else if (this.month >= 10 && this.month <= 12) {
				date += this.month.toString()
			}

			this.accountService.findIncome(date).subscribe({
				next: (res: any) => {
					this.income = res.data
				},
				error: (e: any) => this.global.error(e),
			})
		} else {
			this.accountService.findIncome("").subscribe({
				next: (res: any) => {
					this.income = res.data
				},
				error: (e: any) => this.global.error(e),
			})
		}
	}

}
