import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {GlobalService} from "../global.service";
import {TaskService} from "./task.service";
import {UserService} from "../user/user.service";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {NgIf} from "@angular/common";
import {NavigateDirective} from "../navigate.directive";

@Component({
	selector: 'app-task',
	imports: [
		NgIf,
		ReactiveFormsModule,
		NavigateDirective
	],
	templateUrl: './task.component.html',
	standalone: true
})

export class TaskComponent implements OnInit {

	taskFormGroup = new FormGroup({
		name: new FormControl("", [Validators.required, Validators.minLength(1), Validators.maxLength(255)]),
		address: new FormControl("", [Validators.required, Validators.minLength(1), Validators.maxLength(255)]),
		dateEnd: new FormControl("", [Validators.required, Validators.minLength(1), Validators.maxLength(255)]),
		time: new FormControl("", [Validators.required, Validators.minLength(1), Validators.maxLength(255)]),
		description: new FormControl("", [Validators.required, Validators.minLength(1), Validators.maxLength(255)]),
		intensity: new FormControl(null, [Validators.required, Validators.min(1), Validators.max(1000000)]),
	})

	users: any[] = [];
	userId: any = null;

	tasks: any[] = [];

	constructor(
		private router: Router,
		private global: GlobalService,
		private taskService: TaskService,
		private userService: UserService,
	) {
	}

	get role() {
		return this.global.role;
	}

	ngOnInit(): void {
		if (this.role !== 'USER' && this.role !== 'MANAGER') this.router.navigate(['/login']);

		this.taskService.taskSubject.subscribe(value => {
			this.tasks = value.tasks;
		})
		this.taskService.findAll();

		if (this.role === 'MANAGER') {
			this.userService.userSubject.subscribe(value => {
				this.users = value.roleUser;
			})
			this.userService.findAllByRoleUser();
		}
	}

	changeUserId(event: any) {
		this.userId = event.target.value;
	}

	checkSubmit(): boolean {
		if (this.taskFormGroup.invalid) return false;
		if (this.userId === null) return false;

		return true;
	}

	save() {
		this.taskService.save(this.taskFormGroup.value, this.userId);
	}

}
