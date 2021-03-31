import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from '@angular/common/http';
import {User} from '../../model/entity/user';
import {BaseService} from "../base-service/base.service";
import {Observable} from 'rxjs';
import {catchError, tap} from 'rxjs/operators';
import {ErrorHandler} from '../error-handler';

@Injectable({
  providedIn: 'root'
})
export class UserService extends BaseService<User, User>{

  constructor(protected http: HttpClient) {
    super(http, User);
    this.url += '/users';
  }


  getAuth(login: string, password: string): Observable<HttpResponse<User> | User> {
    return this.http.get<User>(this.url + '/auth/' + login, {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
        Authorization: 'Basic ' + btoa(login + ':' + password),
      }),
      observe: 'response'
    })
      .pipe(
        tap(_ => this.log('got ' + this.TName + ` login=${login}`)),
        catchError(ErrorHandler.handleError<User>('get ' + this.TName + ` login=${login}`))
      );
  }

  createWithResponse(user: User): Observable<string> {
    return this.http.post<string>(this.url, user, {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
      }),
    });
  }

  updateByPoints(user: User): Observable<any> {
    return this.http.put(this.url + '/byPointsChanging', user, this.getAuthHttpHeaders()).pipe(
      tap(_ => this.log('updated points of user' + this.TName + ` id=${user.id}`)),
      catchError(ErrorHandler.handleError<any>('update ' + this.TName))
    );
  }

}
