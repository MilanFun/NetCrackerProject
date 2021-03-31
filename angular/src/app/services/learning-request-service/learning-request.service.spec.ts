import {TestBed} from '@angular/core/testing';
import {LearningRequestService} from './learning-request.service';

describe('LearningRequestService', () => {
  let service: LearningRequestService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LearningRequestService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
