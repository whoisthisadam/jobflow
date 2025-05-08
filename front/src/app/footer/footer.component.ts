import {Component} from '@angular/core';
import {NavigateDirective} from "../navigate.directive";

@Component({
	selector: 'app-footer',
	standalone: true,
	imports: [
		NavigateDirective
	],
	templateUrl: './footer.component.html',
})
export class FooterComponent {

}
