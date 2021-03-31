import {Injectable} from '@angular/core';
import {BaseService} from "../base-service/base.service";
import {HttpClient} from "@angular/common/http";
import {LearnRequest} from "../../model/entity/learn-request";
import {LearnRequestBody} from "../../model/entity/learn-request-body";
import {Observable} from "rxjs";
import {catchError} from "rxjs/operators";
import {ErrorHandler} from "../error-handler";

@Injectable({
  providedIn: 'root'
})
export class LearningRequestService extends BaseService<LearnRequest, LearnRequestBody>{

  constructor(protected http: HttpClient) {
    super(http, LearnRequest);
    this.url += '/requests';
  }

  getByTeacherId(id: number): Observable<LearnRequest[]> {
    return this.http.get<LearnRequest[]>(this.url + '/byTeacherId/' + id, this.getAuthHttpHeaders())
      .pipe(
        catchError(ErrorHandler.handleError<LearnRequest[]>('getLearnRequest ' + `ByTeacherId id=${id}`, []))
      );
  }

  getByStudentId(id: number): Observable<LearnRequest[]> {
    return this.http.get<LearnRequest[]>(this.url + '/byStudentId/' + id, this.getAuthHttpHeaders())
      .pipe(
        catchError(ErrorHandler.handleError<LearnRequest[]>('getLearnRequest ' + `ByTeacherId id=${id}`, []))
      );
  }
  
}
