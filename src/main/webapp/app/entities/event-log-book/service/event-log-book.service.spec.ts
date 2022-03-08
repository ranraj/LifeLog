import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IEventLogBook, EventLogBook } from '../event-log-book.model';

import { EventLogBookService } from './event-log-book.service';

describe('EventLogBook Service', () => {
  let service: EventLogBookService;
  let httpMock: HttpTestingController;
  let elemDefault: IEventLogBook;
  let expectedResult: IEventLogBook | IEventLogBook[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EventLogBookService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      uuid: 'AAAAAAA',
      createdDate: currentDate,
      updatedDate: currentDate,
      name: 'AAAAAAA',
      description: 'AAAAAAA',
      archieved: false,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          createdDate: currentDate.format(DATE_FORMAT),
          updatedDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a EventLogBook', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          createdDate: currentDate.format(DATE_FORMAT),
          updatedDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createdDate: currentDate,
          updatedDate: currentDate,
        },
        returnedFromService
      );

      service.create(new EventLogBook()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a EventLogBook', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          uuid: 'BBBBBB',
          createdDate: currentDate.format(DATE_FORMAT),
          updatedDate: currentDate.format(DATE_FORMAT),
          name: 'BBBBBB',
          description: 'BBBBBB',
          archieved: true,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createdDate: currentDate,
          updatedDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a EventLogBook', () => {
      const patchObject = Object.assign(
        {
          uuid: 'BBBBBB',
          createdDate: currentDate.format(DATE_FORMAT),
          archieved: true,
        },
        new EventLogBook()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          createdDate: currentDate,
          updatedDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of EventLogBook', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          uuid: 'BBBBBB',
          createdDate: currentDate.format(DATE_FORMAT),
          updatedDate: currentDate.format(DATE_FORMAT),
          name: 'BBBBBB',
          description: 'BBBBBB',
          archieved: true,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createdDate: currentDate,
          updatedDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a EventLogBook', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addEventLogBookToCollectionIfMissing', () => {
      it('should add a EventLogBook to an empty array', () => {
        const eventLogBook: IEventLogBook = { id: 123 };
        expectedResult = service.addEventLogBookToCollectionIfMissing([], eventLogBook);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(eventLogBook);
      });

      it('should not add a EventLogBook to an array that contains it', () => {
        const eventLogBook: IEventLogBook = { id: 123 };
        const eventLogBookCollection: IEventLogBook[] = [
          {
            ...eventLogBook,
          },
          { id: 456 },
        ];
        expectedResult = service.addEventLogBookToCollectionIfMissing(eventLogBookCollection, eventLogBook);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a EventLogBook to an array that doesn't contain it", () => {
        const eventLogBook: IEventLogBook = { id: 123 };
        const eventLogBookCollection: IEventLogBook[] = [{ id: 456 }];
        expectedResult = service.addEventLogBookToCollectionIfMissing(eventLogBookCollection, eventLogBook);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(eventLogBook);
      });

      it('should add only unique EventLogBook to an array', () => {
        const eventLogBookArray: IEventLogBook[] = [{ id: 123 }, { id: 456 }, { id: 98468 }];
        const eventLogBookCollection: IEventLogBook[] = [{ id: 123 }];
        expectedResult = service.addEventLogBookToCollectionIfMissing(eventLogBookCollection, ...eventLogBookArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const eventLogBook: IEventLogBook = { id: 123 };
        const eventLogBook2: IEventLogBook = { id: 456 };
        expectedResult = service.addEventLogBookToCollectionIfMissing([], eventLogBook, eventLogBook2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(eventLogBook);
        expect(expectedResult).toContain(eventLogBook2);
      });

      it('should accept null and undefined values', () => {
        const eventLogBook: IEventLogBook = { id: 123 };
        expectedResult = service.addEventLogBookToCollectionIfMissing([], null, eventLogBook, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(eventLogBook);
      });

      it('should return initial array if no EventLogBook is added', () => {
        const eventLogBookCollection: IEventLogBook[] = [{ id: 123 }];
        expectedResult = service.addEventLogBookToCollectionIfMissing(eventLogBookCollection, undefined, null);
        expect(expectedResult).toEqual(eventLogBookCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
