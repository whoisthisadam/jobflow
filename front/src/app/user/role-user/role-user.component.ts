import {Component, OnInit} from '@angular/core';
import {UserService} from "../user.service";
import {Router} from "@angular/router";
import {GlobalService} from "../../global.service";
import {NgIf} from "@angular/common";
import {NavigateDirective} from "../../navigate.directive";

@Component({
	selector: 'app-role-user',
	imports: [
		NgIf,
		NavigateDirective
	],
	templateUrl: './role-user.component.html',
	standalone: true
})

export class RoleUserComponent implements OnInit {

	users: any[] = [];

	constructor(
		private userService: UserService,
		private router: Router,
		private global: GlobalService,
	) {
	}

	get role() {
		return this.global.role;
	}

	ngOnInit() {
		if (this.role !== 'MANAGER' && this.role !== 'ADMIN') this.router.navigate(['/login'])

		this.userService.userSubject.subscribe(value => {
			this.users = value.roleUser;
		})

		this.userService.findAllByRoleUser();
	}

}
