import {Component, OnInit} from '@angular/core';
import {LearnRequest} from "../../model/entity/learn-request";
import {Tag} from "../../model/entity/tag";
import {AppComponent} from "../app.component";
import {TagService} from "../../services/tag-service/tag.service";
import {LearningRequestService} from "../../services/learning-request-service/learning-request.service";
import {LearnRequestBody} from "../../model/entity/learn-request-body";
import {Status} from "../../model/entity/learn-request-status";
import {User} from "../../model/entity/user";
import {UserService} from "../../services/user-service/user.service";

@Component({
  selector: 'app-outgoing-requests',
  templateUrl: './outgoing-requests.component.html',
  styleUrls: ['./outgoing-requests.component.less']
})
export class OutgoingRequestsComponent implements OnInit {
  learns: LearnRequest[];
  tags: Map<number, Tag[]> = new Map<number, Tag[]>();
  Status = Status;

  constructor(private tagService: TagService,
              private learningRequestService: LearningRequestService,
              private userService: UserService) {
  }

  ngOnInit(): void {
    this.learningRequestService.getByStudentId(AppComponent.currentUserId).subscribe(learns => {
      this.learns = learns;
      for (let learn of learns) {
        this.tagService.findWithParents(learn.lesson.tag.id).subscribe(tags => {
          this.tags.set(learn.lesson.tag.id, tags.reverse());
        })
      }
    });
  }
  
  withdraw(learn: LearnRequest): void {
    let body: LearnRequestBody = new LearnRequestBody();
    body.id = learn.id;
    body.teacherId = learn.teacher.id;
    body.studentId = learn.student.id;
    body.lessonId = learn.lesson.id;
    body.status = Status.MEETING_CANCELED;
    body.hiddenForTeacher = learn.hiddenForTeacher;
    body.hiddenForStudent = learn.hiddenForStudent;

    this.userService.getById(learn.student.id).subscribe( teacher => {
      let user: User = new User();
      user.id = teacher.id;
      user.points = teacher.points + learn.lesson.pointsToGet;
      
      this.userService.updateByPoints(user).subscribe(_ => {
        this.learningRequestService.update(body).subscribe();
        this.learns = this.learns.filter(lesson => lesson.id !== body.id);
        AppComponent.currentUserPoints = user.points;
      });
    });
  }
  
  accept(learn: LearnRequest): void {
    // todo действия ниже только после всплывающей менюшки
    let body: LearnRequestBody = new LearnRequestBody();
    body.id = learn.id;
    body.teacherId = learn.teacher.id;
    body.studentId = learn.student.id;
    body.lessonId = learn.lesson.id;
    body.status = Status.MEETING_CONFIRMED;
    body.hiddenForTeacher = learn.hiddenForTeacher;
    body.hiddenForStudent = learn.hiddenForStudent;

    this.userService.getById(learn.teacher.id).subscribe( teacher => {
      let user: User = new User();
      user.id = teacher.id;
      user.points = teacher.points + learn.lesson.pointsToGet;

      this.userService.updateByPoints(user).subscribe(_ => {
        this.learningRequestService.update(body).subscribe();
        this.learns = this.learns.filter(lesson => lesson.id !== body.id);
      });
    });
  }

  deny(learn: LearnRequest): void {
    // todo действия ниже только после всплывающей менюшки
    let body: LearnRequestBody = new LearnRequestBody();
    body.id = learn.id;
    body.teacherId = learn.teacher.id;
    body.studentId = learn.student.id;
    body.lessonId = learn.lesson.id;
    body.status = Status.MEETING_DISPROVED;
    body.hiddenForTeacher = learn.hiddenForTeacher;
    body.hiddenForStudent = learn.hiddenForStudent;

    this.userService.getById(learn.student.id).subscribe( teacher => {
      let user: User = new User();
      user.id = teacher.id;
      user.points = teacher.points + learn.lesson.pointsToGet;

      this.userService.updateByPoints(user).subscribe(_ => {
        this.learningRequestService.update(body).subscribe();
        this.learns = this.learns.filter(lesson => lesson.id !== body.id);
        AppComponent.currentUserPoints = user.points;
      });
    });
  }
  
  
}
