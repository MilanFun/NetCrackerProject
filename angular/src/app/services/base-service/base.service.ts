import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {catchError, tap} from 'rxjs/operators';
import {ErrorHandler} from "../error-handler";
import {AppComponent} from '../../components/app.component';

@Injectable({
  providedIn: 'root'
})
export class BaseService<T extends {id: number}, U extends {id: number}> {
  constructor(protected http: HttpClient, x : new () => T) {
    this.TName = x.name;
  }

  protected url = 'http://localhost:8083';
  protected TName = '';

  getAuthHttpHeaders(): { headers: HttpHeaders } {
    return {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
        Authorization: 'Basic ' + btoa(AppComponent.currentUserLogin + ':' + AppComponent.currentUserPassword)
      })
    };
  }

  getAll(): Observable<T[]> {
    return this.http.get<T[]>(this.url + '/all', this.getAuthHttpHeaders())
      .pipe(
        tap(_ => this.log('got all ' + this.TName + 's')),
        catchError(ErrorHandler.handleError<T[]>('get all ' + this.TName + 's', []))
      );
  }

  getById(id: number): Observable<T> {
    return this.http.get<T>(this.url + '/byId/' + id, this.getAuthHttpHeaders())
      .pipe(
        tap(_ => this.log('got ' + this.TName + ` id=${id}`)),
        catchError(ErrorHandler.handleError<T>('get ' + this.TName + ` id=${id}`))
      );
  }

  delete(id: number): Observable<T> {
    return this.http.delete<T>(`${this.url}/byId/${id}`, this.getAuthHttpHeaders()).pipe(
      tap(_ => this.log('deleted ' + this.TName + ` id=${id}`)),
      catchError(ErrorHandler.handleError<T>('delete ' + this.TName))
    );
  }

  create(u: U): Observable<U> {
    return this.http.post<U>(this.url, u, this.getAuthHttpHeaders())
      .pipe(
        tap((newt: U) => this.log('created ' + this.TName + ` id=${newt.id}`)),
        catchError(ErrorHandler.handleError<U>('create ' + this.TName))
      );
  }

  update(u: U): Observable<any> {
    return this.http.put(this.url, u, this.getAuthHttpHeaders()).pipe(
      tap(_ => this.log('updated ' + this.TName + ` id=${u.id}`)),
      catchError(ErrorHandler.handleError<any>('update ' + this.TName))
    );
  }

  log(message: string): void {
    console.log(message);
  }

}
