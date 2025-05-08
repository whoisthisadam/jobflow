import {Component, OnInit} from '@angular/core';
import {StatsService} from "../stats.service";
import {ChartComponent} from "ng-apexcharts";

@Component({
	selector: 'app-stats-intensity',
	imports: [
		ChartComponent
	],
	templateUrl: './stats-intensity.component.html',
	standalone: true
})

export class StatsIntensityComponent implements OnInit{

	chartOptions: any;

	names:any;
	values:any;

	constructor(
		private statsService: StatsService,
	) {
	}

	ngOnInit(): void {
		this.statsService.intensity().subscribe({
			next: (res:any) => {
				this.names = res.data.names
				this.values = res.data.values
				this.draw()
			}
		})
	}

	draw(){
		this.chartOptions = {
			series: [
				{
					name: "",
					data: this.values
				}
			],
			chart: {
				height: 400,
				type: "bar",
			},
			colors: [
				"#008FFB",
				"#00E396",
				"#FEB019",
				"#FF4560",
				"#775DD0",
				"#546E7A",
				"#26a69a",
				"#D10CE8"
			],
			plotOptions: {
				bar: {
					columnWidth: "45%",
					distributed: true
				}
			},
			dataLabels: {
				enabled: false
			},
			legend: {
				show: false
			},
			grid: {
				show: false
			},
			xaxis: {
				categories: this.names,
				labels: {
					style: {
						colors: [
							"#008FFB",
							"#00E396",
							"#FEB019",
							"#FF4560",
							"#775DD0",
							"#546E7A",
							"#26a69a",
							"#D10CE8"
						],
						fontSize: "12px"
					}
				}
			}
		};
	}

}
