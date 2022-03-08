import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITags, Tags } from '../tags.model';

import { TagsService } from './tags.service';

describe('Tags Service', () => {
  let service: TagsService;
  let httpMock: HttpTestingController;
  let elemDefault: ITags;
  let expectedResult: ITags | ITags[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TagsService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      description: 'AAAAAAA',
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

    it('should create a Tags', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Tags()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Tags', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          description: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Tags', () => {
      const patchObject = Object.assign(
        {
          description: 'BBBBBB',
        },
        new Tags()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Tags', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          description: 'BBBBBB',
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

    it('should delete a Tags', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTagsToCollectionIfMissing', () => {
      it('should add a Tags to an empty array', () => {
        const tags: ITags = { id: 123 };
        expectedResult = service.addTagsToCollectionIfMissing([], tags);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tags);
      });

      it('should not add a Tags to an array that contains it', () => {
        const tags: ITags = { id: 123 };
        const tagsCollection: ITags[] = [
          {
            ...tags,
          },
          { id: 456 },
        ];
        expectedResult = service.addTagsToCollectionIfMissing(tagsCollection, tags);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Tags to an array that doesn't contain it", () => {
        const tags: ITags = { id: 123 };
        const tagsCollection: ITags[] = [{ id: 456 }];
        expectedResult = service.addTagsToCollectionIfMissing(tagsCollection, tags);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tags);
      });

      it('should add only unique Tags to an array', () => {
        const tagsArray: ITags[] = [{ id: 123 }, { id: 456 }, { id: 1879 }];
        const tagsCollection: ITags[] = [{ id: 123 }];
        expectedResult = service.addTagsToCollectionIfMissing(tagsCollection, ...tagsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tags: ITags = { id: 123 };
        const tags2: ITags = { id: 456 };
        expectedResult = service.addTagsToCollectionIfMissing([], tags, tags2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tags);
        expect(expectedResult).toContain(tags2);
      });

      it('should accept null and undefined values', () => {
        const tags: ITags = { id: 123 };
        expectedResult = service.addTagsToCollectionIfMissing([], null, tags, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tags);
      });

      it('should return initial array if no Tags is added', () => {
        const tagsCollection: ITags[] = [{ id: 123 }];
        expectedResult = service.addTagsToCollectionIfMissing(tagsCollection, undefined, null);
        expect(expectedResult).toEqual(tagsCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
