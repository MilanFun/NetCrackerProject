import {ComponentFixture, TestBed} from '@angular/core/testing';

import {OutgoingRequestsComponent} from './outgoing-requests.component';

describe('OutgoingRequestsComponent', () => {
  let component: OutgoingRequestsComponent;
  let fixture: ComponentFixture<OutgoingRequestsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OutgoingRequestsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OutgoingRequestsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
