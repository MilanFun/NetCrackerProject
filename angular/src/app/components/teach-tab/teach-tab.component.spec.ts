import {ComponentFixture, TestBed} from '@angular/core/testing';

import {TeachTabComponent} from './teach-tab.component';

describe('TeachTabComponent', () => {
  let component: TeachTabComponent;
  let fixture: ComponentFixture<TeachTabComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TeachTabComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TeachTabComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
