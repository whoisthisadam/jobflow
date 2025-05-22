import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SalaryManagementComponent } from './salary-management.component';

@NgModule({
  declarations: [

  ],
	imports: [
		CommonModule,
		FormsModule,
		SalaryManagementComponent
	],
  exports: [
    SalaryManagementComponent
  ]
})
export class SalaryManagementModule { }
