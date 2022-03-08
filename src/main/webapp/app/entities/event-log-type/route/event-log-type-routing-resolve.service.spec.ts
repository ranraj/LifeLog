import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IEventLogType, EventLogType } from '../event-log-type.model';
import { EventLogTypeService } from '../service/event-log-type.service';

import { EventLogTypeRoutingResolveService } from './event-log-type-routing-resolve.service';

describe('EventLogType routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: EventLogTypeRoutingResolveService;
  let service: EventLogTypeService;
  let resultEventLogType: IEventLogType | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(EventLogTypeRoutingResolveService);
    service = TestBed.inject(EventLogTypeService);
    resultEventLogType = undefined;
  });

  describe('resolve', () => {
    it('should return IEventLogType returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEventLogType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultEventLogType).toEqual({ id: 123 });
    });

    it('should return new IEventLogType if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEventLogType = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultEventLogType).toEqual(new EventLogType());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as EventLogType })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEventLogType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultEventLogType).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
