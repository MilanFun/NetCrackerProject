import {Component, OnInit} from '@angular/core';
import {User} from "../../model/entity/user";
import {UserService} from "../../services/user-service/user.service";
import {AppComponent} from "../app.component";
import {ModalService} from "../../services/modal-service/modal.service";

@Component({
  selector: 'app-user-page',
  templateUrl: './user-page.component.html',
  styleUrls: ['./user-page.component.less']
})
export class UserPageComponent implements OnInit {

  user: User = new User();
  modalName: string;

  nameText: string;
  surnameText: string;
  groupText: string;
  emailText: string;
  vkText: string;
  telegramText: string;
  phoneText: string;
  
  constructor(private userService: UserService,
              private modalService:ModalService) { }

  ngOnInit(): void {
    this.userService.getById(AppComponent.currentUserId).subscribe(user => {
      this.user = user;
      // todo я хз как это нормально сделать
      this.user.password = user.password == null ? "-" : user.password;
      this.user.group = user.group == null ? "-" : user.group;
      this.user.lastName = user.lastName == null ? "-" : user.lastName;
      this.user.firstName = user.firstName == null ? "-" : user.firstName;
      this.user.phoneNumber = user.phoneNumber == null ? "-" : user.phoneNumber;
      this.user.telegramLink = user.telegramLink == null ? "-" : user.telegramLink;
      this.user.vkLink = user.vkLink == null ? "-" : user.vkLink;
      this.user.email = user.email == null ? "-" : user.email;
    });
  }

  openModal(id: string) {
    this.modalName = id;
    this.modalService.open(id);
  }

  closeModal(id: string) {
    this.modalService.close(id);
  }
  
  save(): void {
    const user: User = new User();
    // todo я хз как это нормально сделать
    if(this.emailText) user.email = this.emailText; else user.email = this.user.email;
    if(this.vkText) user.vkLink = this.vkText; else user.vkLink = this.user.vkLink;
    if(this.telegramText) user.telegramLink = this.telegramText; else user.telegramLink = this.user.telegramLink;
    if(this.phoneText) user.phoneNumber = this.phoneText; else user.phoneNumber = this.user.phoneNumber;
    if(this.nameText) user.firstName = this.nameText; else user.firstName = this.user.firstName;
    if(this.surnameText) user.lastName = this.surnameText; else user.lastName = this.user.lastName;
    if(this.groupText) user.group = this.groupText; else user.group = this.user.group;
    
    user.rating = this.user.rating;
    user.id = AppComponent.currentUserId;
    user.login = AppComponent.currentUserLogin;
    user.password = AppComponent.currentUserPassword;
    
    this.userService.update(user).subscribe(_ => this.user = user);
    this.closeModal(this.modalName);
  }
}
