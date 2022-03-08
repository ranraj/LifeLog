import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEventLogType, EventLogType } from '../event-log-type.model';

import { EventLogTypeService } from './event-log-type.service';

describe('EventLogType Service', () => {
  let service: EventLogTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: IEventLogType;
  let expectedResult: IEventLogType | IEventLogType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EventLogTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      template: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a EventLogType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new EventLogType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a EventLogType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          template: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a EventLogType', () => {
      const patchObject = Object.assign(
        {
          template: 'BBBBBB',
        },
        new EventLogType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of EventLogType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          template: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a EventLogType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addEventLogTypeToCollectionIfMissing', () => {
      it('should add a EventLogType to an empty array', () => {
        const eventLogType: IEventLogType = { id: 123 };
        expectedResult = service.addEventLogTypeToCollectionIfMissing([], eventLogType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(eventLogType);
      });

      it('should not add a EventLogType to an array that contains it', () => {
        const eventLogType: IEventLogType = { id: 123 };
        const eventLogTypeCollection: IEventLogType[] = [
          {
            ...eventLogType,
          },
          { id: 456 },
        ];
        expectedResult = service.addEventLogTypeToCollectionIfMissing(eventLogTypeCollection, eventLogType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a EventLogType to an array that doesn't contain it", () => {
        const eventLogType: IEventLogType = { id: 123 };
        const eventLogTypeCollection: IEventLogType[] = [{ id: 456 }];
        expectedResult = service.addEventLogTypeToCollectionIfMissing(eventLogTypeCollection, eventLogType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(eventLogType);
      });

      it('should add only unique EventLogType to an array', () => {
        const eventLogTypeArray: IEventLogType[] = [{ id: 123 }, { id: 456 }, { id: 42164 }];
        const eventLogTypeCollection: IEventLogType[] = [{ id: 123 }];
        expectedResult = service.addEventLogTypeToCollectionIfMissing(eventLogTypeCollection, ...eventLogTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const eventLogType: IEventLogType = { id: 123 };
        const eventLogType2: IEventLogType = { id: 456 };
        expectedResult = service.addEventLogTypeToCollectionIfMissing([], eventLogType, eventLogType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(eventLogType);
        expect(expectedResult).toContain(eventLogType2);
      });

      it('should accept null and undefined values', () => {
        const eventLogType: IEventLogType = { id: 123 };
        expectedResult = service.addEventLogTypeToCollectionIfMissing([], null, eventLogType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(eventLogType);
      });

      it('should return initial array if no EventLogType is added', () => {
        const eventLogTypeCollection: IEventLogType[] = [{ id: 123 }];
        expectedResult = service.addEventLogTypeToCollectionIfMissing(eventLogTypeCollection, undefined, null);
        expect(expectedResult).toEqual(eventLogTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
