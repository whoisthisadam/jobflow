import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {NgApexchartsModule} from "ng-apexcharts";
import {FormsModule} from "@angular/forms";
import {GlobalService} from "../global.service";
import {StatsCategoriesComponent} from "./stats-categories/stats-categories.component";
import {StatsIntensityComponent} from "./stats-intensity/stats-intensity.component";
import {UserService} from "../user/user.service";

@Component({
	selector: 'app-stats',
	standalone: true,
	imports: [
		NgApexchartsModule,
		FormsModule,
		StatsCategoriesComponent,
		StatsIntensityComponent
	],
	templateUrl: './stats.component.html',
})

export class StatsComponent implements OnInit {

	users: any[] = [];

	constructor(
		private router: Router,
		private global: GlobalService,
		private userService: UserService,
	) {
	}

	ngOnInit(): void {
		if (this.global.role !== 'ADMIN') this.router.navigate(['/login']);

		this.userService.userSubject.subscribe(value => {
			this.users = value.roleUser;
		})
		this.userService.findAllByRoleUser();
	}

}
