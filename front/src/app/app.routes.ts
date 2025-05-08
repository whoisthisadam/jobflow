import {Routes} from '@angular/router';
import {ErrorComponent} from "./error/error.component";
import {StatsComponent} from "./stats/stats.component";
import {UserComponent} from "./user/user.component";
import {LoginComponent} from "./auth/login/login.component";
import {RegComponent} from "./auth/reg/reg.component";
import {MainComponent} from "./main/main.component";
import {CategoryComponent} from "./category/category.component";
import {AccountComponent} from "./account/account.component";
import {UserPageComponent} from "./user/user-page/user-page.component";
import {RoleUserComponent} from "./user/role-user/role-user.component";
import {TaskComponent} from "./task/task.component";
import {TaskPageComponent} from "./task/task-page/task-page.component";

export const routes: Routes = [

	{path: "", component: MainComponent},

	{path: "reg", component: RegComponent},
	{path: "login", component: LoginComponent},

	{path: "users", component: UserComponent},
	{path: "users_role_user", component: RoleUserComponent},
	{path: "user", component: UserPageComponent},

	{path: "account", component: AccountComponent},

	{path: "categories", component: CategoryComponent},

	{path: "tasks", component: TaskComponent},
	{path: "task", component: TaskPageComponent},

	{path: "stats", component: StatsComponent},

	{path: "error", component: ErrorComponent},
	{path: "**", component: ErrorComponent},

];
