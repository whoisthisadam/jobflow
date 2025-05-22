import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {GlobalService} from "../global.service";
import {BehaviorSubject} from 'rxjs';
import {AlertService} from '../alert/alert.service';

@Injectable({
  providedIn: 'root'
})
export class SalaryService {

  salarySubject = new BehaviorSubject<any>({
    salaries: [],
    currentUserSalaries: []
  });

  constructor(
    private http: HttpClient,
    private router: Router,
    private global: GlobalService,
    private alert: AlertService
  ) { }

  private get url() {
    return this.global.backendURL + '/salary';
  }

  private error(e: any) {
    console.log(e.error);
    this.alert.showAlertMessage(e.error.message);
  }

  findAll() {
    return this.http.get(
      this.url + '/all',
      { headers: this.global.headersToken }
    ).subscribe({
      next: (res: any) =>
        this.salarySubject.next({
          ...this.salarySubject.value,
          salaries: res.data,
        }),
      error: (e: any) => this.error(e)
    });
  }

  findByUser(userId: number) {
    return this.http.get(
      this.url + `/user/${userId}`,
      { headers: this.global.headersToken }
    );
  }

  findByCurrentUser() {
    return this.http.get(
      this.url + '/current',
      { headers: this.global.headersToken }
    ).subscribe({
      next: (res: any) =>
        this.salarySubject.next({
          ...this.salarySubject.value,
          currentUserSalaries: res.data,
        }),
      error: (e: any) => this.error(e)
    });
  }

  findByUserAndYearAndMonth(userId: number, year: number, month: number) {
    return this.http.get(
      this.url + `/user/${userId}/year/${year}/month/${month}`,
      { headers: this.global.headersToken }
    );
  }

  findByCurrentUserAndYearAndMonth(year: number, month: number) {
    return this.http.get(
      this.url + `/current/year/${year}/month/${month}`,
      { headers: this.global.headersToken }
    );
  }

  save(salary: any) {
    return this.http.post(
      this.url,
      salary,
      { headers: this.global.headersToken }
    );
  }

  update(salary: any) {
    return this.http.put(
      this.url,
      salary,
      { headers: this.global.headersToken }
    );
  }

  delete(id: number) {
    return this.http.delete(
      this.url + `/${id}`,
      { headers: this.global.headersToken }
    );
  }
}
