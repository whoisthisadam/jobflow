import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {GlobalService} from "../../global.service";
import {TaskService} from "../task.service";
import {NgIf} from "@angular/common";
import {NavigateDirective} from "../../navigate.directive";
import {TaskReportService} from "../task-report.service";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";

@Component({
	selector: 'app-task-page',
	imports: [
		NgIf,
		NavigateDirective,
		ReactiveFormsModule
	],
	templateUrl: './task-page.component.html',
	standalone: true
})

export class TaskPageComponent implements OnInit {

	task: any = {
		name: '',
	}

	get reports() {
		return this.task.reports;
	}

	constructor(
		private router: Router,
		private global: GlobalService,
		private taskService: TaskService,
		private activatedRoute: ActivatedRoute,
		private reportService: TaskReportService,
	) {
	}

	get role() {
		return this.global.role;
	}

	ngOnInit(): void {
		if (this.role !== 'USER' && this.role !== 'MANAGER') this.router.navigate(['/login']);

		this.activatedRoute.queryParams.subscribe(value => {
			this.taskService.find(value['id']).subscribe({
				next: (res: any) => {
					this.task = res.data;
				},
				error: (e: any) => {
					console.log(e.error)
					if (e.error.code === 404) {
						this.router.navigate(['/error'], {queryParams: {message: e.error.message}});
					} else {
						this.router.navigate(['/login']);
					}
				}
			})
		})
	}

	work() {
		this.taskService.work(this.task.id).subscribe({
			next: (res: any) => this.task = res.data,
			error: (e: any) => this.global.error(e),
		})
	}

	done() {
		this.taskService.done(this.task.id).subscribe({
			next: (res: any) => this.task = res.data,
			error: (e: any) => this.global.error(e),
		})
	}

	reportFormGroup = new FormGroup({
		name: new FormControl("", [Validators.required, Validators.minLength(1), Validators.maxLength(255)]),
	})

	img: any = null;
	file: any = null;

	changeImg(event: any) {
		this.img = event.target.files;
	}

	changeFile(event: any) {
		this.file = event.target.files;
	}

	checkReport() {
		if (this.reportFormGroup.invalid) return false;
		if (this.img === null) return false;
		if (this.file === null) return false;

		return true;
	}

	report() {
		this.reportService.save(this.reportFormGroup.value, this.task.id).subscribe({
			next: (res: any) => {
				this.reportService.updateImg(res.data.id, this.img).subscribe({
					next: (res: any) => {
						this.reportService.updateFile(res.data.id, this.file).subscribe({
							next: (res: any) => {
								this.reports.unshift(res.data)
								this.reportFormGroup.reset();
								this.img = null;
								this.file = null;
							},
							error: (e: any) => this.global.error(e),
						})
					},
					error: (e: any) => this.global.error(e),
				})
			},
			error: (e: any) => this.global.error(e),
		})
	}

}
