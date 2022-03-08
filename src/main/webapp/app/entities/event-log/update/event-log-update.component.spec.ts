import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EventLogService } from '../service/event-log.service';
import { IEventLog, EventLog } from '../event-log.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ITags } from 'app/entities/tags/tags.model';
import { TagsService } from 'app/entities/tags/service/tags.service';
import { IEventLogBook } from 'app/entities/event-log-book/event-log-book.model';
import { EventLogBookService } from 'app/entities/event-log-book/service/event-log-book.service';

import { EventLogUpdateComponent } from './event-log-update.component';

describe('EventLog Management Update Component', () => {
  let comp: EventLogUpdateComponent;
  let fixture: ComponentFixture<EventLogUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let eventLogService: EventLogService;
  let userService: UserService;
  let tagsService: TagsService;
  let eventLogBookService: EventLogBookService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EventLogUpdateComponent],
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
      .overrideTemplate(EventLogUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EventLogUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    eventLogService = TestBed.inject(EventLogService);
    userService = TestBed.inject(UserService);
    tagsService = TestBed.inject(TagsService);
    eventLogBookService = TestBed.inject(EventLogBookService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const eventLog: IEventLog = { id: 456 };
      const user: IUser = { id: 88471 };
      eventLog.user = user;

      const userCollection: IUser[] = [{ id: 48847 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ eventLog });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Tags query and add missing value', () => {
      const eventLog: IEventLog = { id: 456 };
      const tags: ITags[] = [{ id: 84546 }];
      eventLog.tags = tags;

      const tagsCollection: ITags[] = [{ id: 4811 }];
      jest.spyOn(tagsService, 'query').mockReturnValue(of(new HttpResponse({ body: tagsCollection })));
      const additionalTags = [...tags];
      const expectedCollection: ITags[] = [...additionalTags, ...tagsCollection];
      jest.spyOn(tagsService, 'addTagsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ eventLog });
      comp.ngOnInit();

      expect(tagsService.query).toHaveBeenCalled();
      expect(tagsService.addTagsToCollectionIfMissing).toHaveBeenCalledWith(tagsCollection, ...additionalTags);
      expect(comp.tagsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call EventLogBook query and add missing value', () => {
      const eventLog: IEventLog = { id: 456 };
      const eventLogBook: IEventLogBook = { id: 27787 };
      eventLog.eventLogBook = eventLogBook;

      const eventLogBookCollection: IEventLogBook[] = [{ id: 13665 }];
      jest.spyOn(eventLogBookService, 'query').mockReturnValue(of(new HttpResponse({ body: eventLogBookCollection })));
      const additionalEventLogBooks = [eventLogBook];
      const expectedCollection: IEventLogBook[] = [...additionalEventLogBooks, ...eventLogBookCollection];
      jest.spyOn(eventLogBookService, 'addEventLogBookToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ eventLog });
      comp.ngOnInit();

      expect(eventLogBookService.query).toHaveBeenCalled();
      expect(eventLogBookService.addEventLogBookToCollectionIfMissing).toHaveBeenCalledWith(
        eventLogBookCollection,
        ...additionalEventLogBooks
      );
      expect(comp.eventLogBooksSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const eventLog: IEventLog = { id: 456 };
      const user: IUser = { id: 64412 };
      eventLog.user = user;
      const tags: ITags = { id: 60226 };
      eventLog.tags = [tags];
      const eventLogBook: IEventLogBook = { id: 41439 };
      eventLog.eventLogBook = eventLogBook;

      activatedRoute.data = of({ eventLog });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(eventLog));
      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.tagsSharedCollection).toContain(tags);
      expect(comp.eventLogBooksSharedCollection).toContain(eventLogBook);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<EventLog>>();
      const eventLog = { id: 123 };
      jest.spyOn(eventLogService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ eventLog });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: eventLog }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(eventLogService.update).toHaveBeenCalledWith(eventLog);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<EventLog>>();
      const eventLog = new EventLog();
      jest.spyOn(eventLogService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ eventLog });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: eventLog }));
      saveSubject.complete();

      // THEN
      expect(eventLogService.create).toHaveBeenCalledWith(eventLog);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<EventLog>>();
      const eventLog = { id: 123 };
      jest.spyOn(eventLogService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ eventLog });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(eventLogService.update).toHaveBeenCalledWith(eventLog);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackUserById', () => {
      it('Should return tracked User primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUserById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackTagsById', () => {
      it('Should return tracked Tags primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTagsById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackEventLogBookById', () => {
      it('Should return tracked EventLogBook primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEventLogBookById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedTags', () => {
      it('Should return option if no Tags is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedTags(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Tags for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedTags(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Tags is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedTags(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
