import {ComponentFixture, TestBed} from '@angular/core/testing';

import {SignInTabComponent} from './sign-in-tab.component';

describe('SignInTabComponent', () => {
  let component: SignInTabComponent;
  let fixture: ComponentFixture<SignInTabComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SignInTabComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SignInTabComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
