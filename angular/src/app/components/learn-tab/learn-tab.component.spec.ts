import {ComponentFixture, TestBed} from '@angular/core/testing';

import {LearnTabComponent} from './learn-tab.component';

describe('LearnTabComponent', () => {
  let component: LearnTabComponent;
  let fixture: ComponentFixture<LearnTabComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LearnTabComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LearnTabComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
