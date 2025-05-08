import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {BehaviorSubject} from "rxjs";
import {Router} from "@angular/router";
import {GlobalService} from "../global.service";
import {AlertService} from "../alert/alert.service";

@Injectable({
	providedIn: 'root'
})

export class UserService {

	userSubject = new BehaviorSubject<any>({
		users: [],
		roleUser: [],
	})

	constructor(
		private http: HttpClient,
		private router: Router,
		private global: GlobalService,
		private alert: AlertService
	) {
	}

	private get url() {
		return this.global.backendURL + '/users'
	}

	private error(e: any) {
		console.log(e.error);
		this.alert.showAlertMessage(e.error.message);
	}

	findAll() {
		return this.http.get(
			this.url + '/all',
			{headers: this.global.headersToken}
		).subscribe({
			next: (res: any) =>
				this.userSubject.next({
					...this.userSubject.value,
					users: res.data,
				}),
			error: (e: any) => this.error(e)
		});
	}

	findAllByRoleUser() {
		return this.http.get(
			this.url + '/role/user',
			{headers: this.global.headersToken}
		).subscribe({
			next: (res: any) =>
				this.userSubject.next({
					...this.userSubject.value,
					roleUser: res.data,
				}),
			error: (e: any) => this.error(e)
		});
	}

	find(id: number) {
		return this.http.get(
			this.url + `/${id}`,
			{headers: this.global.headersToken}
		);
	}

	updateRole(id: number, role: string) {
		return this.http.patch(
			this.url + `/${id}/role`,
			"",
			{
				headers: this.global.headersToken,
				params: new HttpParams().appendAll({role: role})
			}
		).subscribe({
			error: (e: any) => this.error(e)
		});
	}

	updateCategory(id: number, categoryId: number) {
		return this.http.patch(
			this.url + `/${id}/category`,
			"",
			{
				headers: this.global.headersToken,
				params: new HttpParams().appendAll({categoryId: categoryId})
			}
		);
	}

	updateExp(id: number, exp: number) {
		return this.http.patch(
			this.url + `/${id}/exp`,
			"",
			{
				headers: this.global.headersToken,
				params: new HttpParams().appendAll({exp: exp})
			}
		);
	}

	delete(id: number) {
		return this.http.delete(
			this.url + `/${id}`,
			{headers: this.global.headersToken}
		).subscribe({
			next: () => {
				let users = this.userSubject.value.users;
				users = users.filter((i: any) => i.id !== id);
				this.userSubject.next({
					...this.userSubject.value,
					users: users
				});
			},
			error: (e: any) => this.error(e)
		});
	}

}
