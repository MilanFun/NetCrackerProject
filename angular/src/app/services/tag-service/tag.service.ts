import {Injectable} from '@angular/core';
import {BaseService} from "../base-service/base.service";
import {HttpClient} from "@angular/common/http";
import {Tag} from "../../model/entity/tag";
import {Observable} from "rxjs";
import {catchError} from "rxjs/operators";
import {ErrorHandler} from "../error-handler";

@Injectable({
  providedIn: 'root'
})
export class TagService extends BaseService<Tag, Tag>{

  constructor(protected http: HttpClient) {
    super(http, Tag);
    this.url += '/tags';
  }

  getChildren(id: number): Observable<Tag[]> {
    return this.http.get<Tag[]>(this.url + '/children/' + id, this.getAuthHttpHeaders())
      .pipe(
        catchError(ErrorHandler.handleError<Tag[]>('getChildren ' + `ById id=${id}`, []))
      );
  }
  
  getLastId(): Observable<number> {
    return this.http.get<number>(this.url + '/lastTagId', this.getAuthHttpHeaders())
      .pipe(
        catchError(ErrorHandler.handleError<number>('getLastId', 0))
      );
  }

  findWithParents(id: number): Observable<Tag[]> {
    return this.http.get<Tag[]>(this.url + '/withParentsById/' + id, this.getAuthHttpHeaders())
      .pipe(
        catchError(ErrorHandler.handleError<Tag[]>('findWithParents ' + `ById id=${id}`, []))
      );
  }

}
