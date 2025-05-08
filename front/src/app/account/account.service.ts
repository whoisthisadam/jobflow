import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Router} from "@angular/router";
import {GlobalService} from "../global.service";

@Injectable({
	providedIn: 'root'
})

export class AccountService {

	constructor(
		private http: HttpClient,
		private router: Router,
		private global: GlobalService,
	) {
	}

	private get url() {
		return this.global.backendURL + '/users'
	}

	find() {
		return this.http.get(
			this.url,
			{headers: this.global.headersToken}
		);
	}

	findIncome(date: string) {
		return this.http.get(
			this.url + '/income',
			{
				headers: this.global.headersToken,
				params: new HttpParams().appendAll({date: date})
			}
		);
	}

	updateFio(fio: string, exp: number) {
		return this.http.patch(
			this.url + '/fio',
			"",
			{
				headers: this.global.headersToken,
				params: new HttpParams().appendAll({
					fio: fio,
					exp: exp,
				})
			}
		);
	}

	updateImg(file: any) {
		let formData = new FormData();
		for (let i = 0; i < file.length; i++) {
			formData.append('file', file[i]);
		}
		return this.http.patch(
			this.url + '/img',
			formData,
			{headers: this.global.headersMultipartToken}
		);
	}

}
