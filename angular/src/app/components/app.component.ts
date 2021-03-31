import {Component, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {Router} from '@angular/router';
import {UserService} from "../services/user-service/user.service";
import {Observable} from "rxjs";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.less']
})
export class AppComponent implements OnInit, OnChanges {

  public static currentUserId: number = 1;
  public static currentUserLogin: string = "kekich";
  public static currentUserPassword: string = "0000";
  public static currentUserPoints: number;
  public static isAdmin: boolean = false;
  _isAdmin$: Observable<boolean>;
  disableTagButton: any;
  
  constructor(public router: Router,
              private userService: UserService) {
  }

  ngOnChanges(changes: SimpleChanges): void {
    console.log(changes);
  }

  ngOnInit(): void {
    this.disableTagButton = {'visibility': 'hidden'};
    // this._isAdmin$ = AppComponent.isAdmin;
    this.userService.getById(AppComponent.currentUserId).subscribe(user => {
      console.log(user.role);
      
      // this._isAdmin$ = (user.role === 'ADMIN');
    });
  }
  
  
}
