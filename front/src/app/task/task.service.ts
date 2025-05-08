import {Injectable} from '@angular/core';
import {BehaviorSubject} from "rxjs";
import {HttpClient, HttpParams} from "@angular/common/http";
import {GlobalService} from "../global.service";
import {Router} from "@angular/router";

@Injectable({
	providedIn: 'root'
})

export class TaskService {

	taskSubject = new BehaviorSubject<any>({
		tasks: [],
	})

	constructor(
		private http: HttpClient,
		private global: GlobalService,
		private router: Router
	) {
	}

	private get url() {
		return this.global.backendURL + '/tasks'
	}

	findAll() {
		this.http.get(
			this.url,
			{headers: this.global.headersToken}
		).subscribe({
			next: (res: any) => this.taskSubject.next({
				...this.taskSubject.value,
				tasks: res.data,
			}),
			error: (e: any) => this.global.error(e),
		})
	}

	find(id: number) {
		return this.http.get(
			this.url + `/${id}`,
			{headers: this.global.headersToken}
		);
	}

	save(task: any, userId: number) {
		this.http.post(
			this.url,
			JSON.stringify(task),
			{
				headers: this.global.headersJsonToken,
				params: new HttpParams().appendAll({userId: userId}),
			}
		).subscribe({
			next: (res: any) => this.router.navigate(['/task'], {queryParams: {id: res.data.id}}),
			error: (e: any) => this.global.error(e),
		})
	}

	work(id: number) {
		return this.http.patch(
			this.url + `/${id}/work`,
			"",
			{headers: this.global.headersToken}
		);
	}

	done(id: number) {
		return this.http.patch(
			this.url + `/${id}/done`,
			"",
			{headers: this.global.headersToken}
		);
	}

}
