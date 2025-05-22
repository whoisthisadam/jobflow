import { ComponentFixture, TestBed } from '@angular/core/testing';
import { StatsCategoriesComponent } from './stats-categories.component';
import { StatsService } from '../stats.service';
import { of } from 'rxjs';
import { ChartComponent } from 'ng-apexcharts';

describe('StatsCategoriesComponent', () => {
  let component: StatsCategoriesComponent;
  let fixture: ComponentFixture<StatsCategoriesComponent>;
  let statsServiceSpy: jasmine.SpyObj<StatsService>;

  beforeEach(async () => {
    // Create a spy for StatsService
    const spy = jasmine.createSpyObj('StatsService', ['categories']);

    await TestBed.configureTestingModule({
      imports: [StatsCategoriesComponent, ChartComponent],
      providers: [
        { provide: StatsService, useValue: spy }
      ]
    }).compileComponents();

    statsServiceSpy = TestBed.inject(StatsService) as jasmine.SpyObj<StatsService>;
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StatsCategoriesComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    // Mock the categories method to return sample data
    statsServiceSpy.categories.and.returnValue(of({
      data: {
        names: ['Category 1', 'Category 2', 'Category 3'],
        values: [10, 20, 30]
      }
    }));
    
    fixture.detectChanges(); // This will trigger ngOnInit
    
    expect(component).toBeTruthy();
  });

  describe('draw method', () => {
    it('should set chartOptions with correct data', () => {
      // Set up test data
      component.names = ['Category A', 'Category B', 'Category C'];
      component.values = [15, 25, 35];
      
      // Call the method to test
      component.draw();
      
      // Verify the chartOptions are set correctly
      expect(component.chartOptions).toBeDefined();
      expect(component.chartOptions.labels).toEqual(['Category A', 'Category B', 'Category C']);
      expect(component.chartOptions.series).toEqual([15, 25, 35]);
      expect(component.chartOptions.chart.type).toEqual('pie');
      expect(component.chartOptions.chart.height).toEqual(400);
    });
    
    it('should set responsive options correctly', () => {
      // Set up minimal test data
      component.names = ['Test'];
      component.values = [100];
      
      // Call the method to test
      component.draw();
      
      // Verify responsive options
      expect(component.chartOptions.responsive).toBeDefined();
      expect(component.chartOptions.responsive.length).toEqual(1);
      expect(component.chartOptions.responsive[0].breakpoint).toEqual(480);
      expect(component.chartOptions.responsive[0].options.chart.width).toEqual(200);
      expect(component.chartOptions.responsive[0].options.legend.position).toEqual('bottom');
    });
    
    it('should handle empty data', () => {
      // Set up empty test data
      component.names = [];
      component.values = [];
      
      // Call the method to test
      component.draw();
      
      // Verify the chartOptions are still set correctly with empty arrays
      expect(component.chartOptions.labels).toEqual([]);
      expect(component.chartOptions.series).toEqual([]);
    });
  });

  it('should call draw method after receiving data from service', () => {
    // Set up spy on the draw method
    spyOn(component, 'draw');
    
    // Mock the categories method to return sample data
    statsServiceSpy.categories.and.returnValue(of({
      data: {
        names: ['Category X', 'Category Y'],
        values: [40, 60]
      }
    }));
    
    // Trigger ngOnInit
    fixture.detectChanges();
    
    // Verify the service was called
    expect(statsServiceSpy.categories).toHaveBeenCalled();
    
    // Verify the data was set correctly
    expect(component.names).toEqual(['Category X', 'Category Y']);
    expect(component.values).toEqual([40, 60]);
    
    // Verify draw was called
    expect(component.draw).toHaveBeenCalled();
  });
});
