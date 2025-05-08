import {Component, OnInit} from '@angular/core';
import {UserService} from "../user.service";
import {ActivatedRoute, Router} from "@angular/router";
import {GlobalService} from "../../global.service";
import {CategoryService} from "../../category/category.service";
import {NavigateDirective} from "../../navigate.directive";
import {NgIf} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {AlertService} from "../../alert/alert.service";

@Component({
	selector: 'app-user-page',
	imports: [
		NavigateDirective,
		NgIf,
		FormsModule
	],
	templateUrl: './user-page.component.html',
	standalone: true
})

export class UserPageComponent implements OnInit {

	user: any = {
		fio: '',
	};

	categories: any[] = [];

	constructor(
		private userService: UserService,
		private router: Router,
		private global: GlobalService,
		private categoryService: CategoryService,
		private activatedRoute: ActivatedRoute,
		private alert: AlertService,
	) {
	}

	get role() {
		return this.global.role;
	}

	ngOnInit() {
		if (this.role !== 'MANAGER' && this.role !== 'ADMIN') this.router.navigate(['/login'])

		this.activatedRoute.queryParams.subscribe(params => {
			this.userService.find(params['id']).subscribe({
				next: (res: any) => {
					this.user = res.data;
				},
				error: (e: any) => {
					console.log(e.error)
					if (e.error.code === 404) {
						this.router.navigate(['/error'], {queryParams: {message: e.error.message}});
					} else {
						this.router.navigate(['/login'])
					}
				}
			})
		})

		this.categoryService.categorySubject.subscribe(value => {
			this.categories = value.categories;
		})
		this.categoryService.findAll();
	}

	updateCategory(event: any) {
		this.userService.updateCategory(this.user.id, event.target.value).subscribe({
			next: (res: any) => this.user = res.data,
			error: (e: any) => this.global.error(e),
		})
	}

	updateExp() {
		this.userService.updateExp(this.user.id, this.user.exp).subscribe({
			next: (res: any) => {
				this.alert.showAlertMessage("Стаж обновлен");
				this.user = res.data;
			},
			error: (e: any) => this.global.error(e),
		})
	}

}
