import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EventLogTypeService } from '../service/event-log-type.service';
import { IEventLogType, EventLogType } from '../event-log-type.model';
import { IEventLog } from 'app/entities/event-log/event-log.model';
import { EventLogService } from 'app/entities/event-log/service/event-log.service';

import { EventLogTypeUpdateComponent } from './event-log-type-update.component';

describe('EventLogType Management Update Component', () => {
  let comp: EventLogTypeUpdateComponent;
  let fixture: ComponentFixture<EventLogTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let eventLogTypeService: EventLogTypeService;
  let eventLogService: EventLogService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EventLogTypeUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(EventLogTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EventLogTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    eventLogTypeService = TestBed.inject(EventLogTypeService);
    eventLogService = TestBed.inject(EventLogService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call EventLog query and add missing value', () => {
      const eventLogType: IEventLogType = { id: 456 };
      const eventLog: IEventLog = { id: 94512 };
      eventLogType.eventLog = eventLog;

      const eventLogCollection: IEventLog[] = [{ id: 86460 }];
      jest.spyOn(eventLogService, 'query').mockReturnValue(of(new HttpResponse({ body: eventLogCollection })));
      const additionalEventLogs = [eventLog];
      const expectedCollection: IEventLog[] = [...additionalEventLogs, ...eventLogCollection];
      jest.spyOn(eventLogService, 'addEventLogToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ eventLogType });
      comp.ngOnInit();

      expect(eventLogService.query).toHaveBeenCalled();
      expect(eventLogService.addEventLogToCollectionIfMissing).toHaveBeenCalledWith(eventLogCollection, ...additionalEventLogs);
      expect(comp.eventLogsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const eventLogType: IEventLogType = { id: 456 };
      const eventLog: IEventLog = { id: 86405 };
      eventLogType.eventLog = eventLog;

      activatedRoute.data = of({ eventLogType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(eventLogType));
      expect(comp.eventLogsSharedCollection).toContain(eventLog);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<EventLogType>>();
      const eventLogType = { id: 123 };
      jest.spyOn(eventLogTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ eventLogType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: eventLogType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(eventLogTypeService.update).toHaveBeenCalledWith(eventLogType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<EventLogType>>();
      const eventLogType = new EventLogType();
      jest.spyOn(eventLogTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ eventLogType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: eventLogType }));
      saveSubject.complete();

      // THEN
      expect(eventLogTypeService.create).toHaveBeenCalledWith(eventLogType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<EventLogType>>();
      const eventLogType = { id: 123 };
      jest.spyOn(eventLogTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ eventLogType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(eventLogTypeService.update).toHaveBeenCalledWith(eventLogType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackEventLogById', () => {
      it('Should return tracked EventLog primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEventLogById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
