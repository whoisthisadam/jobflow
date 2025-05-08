import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {GlobalService} from "../global.service";
import {Router} from "@angular/router";

@Injectable({
	providedIn: 'root'
})

export class TaskReportService {

	constructor(
		private http: HttpClient,
		private global: GlobalService,
		private router: Router
	) {
	}

	private get url() {
		return this.global.backendURL + '/tasks/reports'
	}

	find(id: number) {
		return this.http.get(
			this.url + `/${id}`,
			{headers: this.global.headersToken}
		);
	}

	save(report: any, taskId: number) {
		return this.http.post(
			this.url,
			JSON.stringify(report),
			{
				headers: this.global.headersJsonToken,
				params: new HttpParams().appendAll({taskId: taskId}),
			}
		)
	}

	updateImg(id: number, file: any) {
		let formData = new FormData();
		for (let i = 0; i < file.length; i++) {
			formData.append('file', file[i]);
		}
		return this.http.patch(
			this.url + `/${id}/img`,
			formData,
			{headers: this.global.headersMultipartToken}
		)
	}

	updateFile(id: number, file: any) {
		let formData = new FormData();
		for (let i = 0; i < file.length; i++) {
			formData.append('file', file[i]);
		}
		return this.http.patch(
			this.url + `/${id}/file`,
			formData,
			{headers: this.global.headersMultipartToken}
		)
	}

}
