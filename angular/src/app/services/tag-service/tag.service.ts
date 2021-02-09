import { Injectable } from '@angular/core';
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
    return this.http.get<Tag[]>(this.url + '/children/' + id)
      .pipe(
        catchError(ErrorHandler.handleError<Tag[]>('getChildren ' + `ById id=${id}`, []))
      );
  }

}
