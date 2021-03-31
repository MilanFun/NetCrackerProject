import {TestBed} from '@angular/core/testing';

import {BaseService} from './base.service';

describe('DefaultService', () => {
  let service: BaseService<any, any>;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BaseService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
