import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEventLogBook, getEventLogBookIdentifier } from '../event-log-book.model';

export type EntityResponseType = HttpResponse<IEventLogBook>;
export type EntityArrayResponseType = HttpResponse<IEventLogBook[]>;

@Injectable({ providedIn: 'root' })
export class EventLogBookService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/event-log-books');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(eventLogBook: IEventLogBook): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(eventLogBook);
    return this.http
      .post<IEventLogBook>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(eventLogBook: IEventLogBook): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(eventLogBook);
    return this.http
      .put<IEventLogBook>(`${this.resourceUrl}/${getEventLogBookIdentifier(eventLogBook) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(eventLogBook: IEventLogBook): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(eventLogBook);
    return this.http
      .patch<IEventLogBook>(`${this.resourceUrl}/${getEventLogBookIdentifier(eventLogBook) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEventLogBook>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEventLogBook[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEventLogBookToCollectionIfMissing(
    eventLogBookCollection: IEventLogBook[],
    ...eventLogBooksToCheck: (IEventLogBook | null | undefined)[]
  ): IEventLogBook[] {
    const eventLogBooks: IEventLogBook[] = eventLogBooksToCheck.filter(isPresent);
    if (eventLogBooks.length > 0) {
      const eventLogBookCollectionIdentifiers = eventLogBookCollection.map(
        eventLogBookItem => getEventLogBookIdentifier(eventLogBookItem)!
      );
      const eventLogBooksToAdd = eventLogBooks.filter(eventLogBookItem => {
        const eventLogBookIdentifier = getEventLogBookIdentifier(eventLogBookItem);
        if (eventLogBookIdentifier == null || eventLogBookCollectionIdentifiers.includes(eventLogBookIdentifier)) {
          return false;
        }
        eventLogBookCollectionIdentifiers.push(eventLogBookIdentifier);
        return true;
      });
      return [...eventLogBooksToAdd, ...eventLogBookCollection];
    }
    return eventLogBookCollection;
  }

  protected convertDateFromClient(eventLogBook: IEventLogBook): IEventLogBook {
    return Object.assign({}, eventLogBook, {
      createdDate: eventLogBook.createdDate?.isValid() ? eventLogBook.createdDate.format(DATE_FORMAT) : undefined,
      updatedDate: eventLogBook.updatedDate?.isValid() ? eventLogBook.updatedDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdDate = res.body.createdDate ? dayjs(res.body.createdDate) : undefined;
      res.body.updatedDate = res.body.updatedDate ? dayjs(res.body.updatedDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((eventLogBook: IEventLogBook) => {
        eventLogBook.createdDate = eventLogBook.createdDate ? dayjs(eventLogBook.createdDate) : undefined;
        eventLogBook.updatedDate = eventLogBook.updatedDate ? dayjs(eventLogBook.updatedDate) : undefined;
      });
    }
    return res;
  }
}
