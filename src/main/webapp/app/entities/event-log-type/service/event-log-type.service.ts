import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEventLogType, getEventLogTypeIdentifier } from '../event-log-type.model';

export type EntityResponseType = HttpResponse<IEventLogType>;
export type EntityArrayResponseType = HttpResponse<IEventLogType[]>;

@Injectable({ providedIn: 'root' })
export class EventLogTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/event-log-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(eventLogType: IEventLogType): Observable<EntityResponseType> {
    return this.http.post<IEventLogType>(this.resourceUrl, eventLogType, { observe: 'response' });
  }

  update(eventLogType: IEventLogType): Observable<EntityResponseType> {
    return this.http.put<IEventLogType>(`${this.resourceUrl}/${getEventLogTypeIdentifier(eventLogType) as number}`, eventLogType, {
      observe: 'response',
    });
  }

  partialUpdate(eventLogType: IEventLogType): Observable<EntityResponseType> {
    return this.http.patch<IEventLogType>(`${this.resourceUrl}/${getEventLogTypeIdentifier(eventLogType) as number}`, eventLogType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEventLogType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEventLogType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEventLogTypeToCollectionIfMissing(
    eventLogTypeCollection: IEventLogType[],
    ...eventLogTypesToCheck: (IEventLogType | null | undefined)[]
  ): IEventLogType[] {
    const eventLogTypes: IEventLogType[] = eventLogTypesToCheck.filter(isPresent);
    if (eventLogTypes.length > 0) {
      const eventLogTypeCollectionIdentifiers = eventLogTypeCollection.map(
        eventLogTypeItem => getEventLogTypeIdentifier(eventLogTypeItem)!
      );
      const eventLogTypesToAdd = eventLogTypes.filter(eventLogTypeItem => {
        const eventLogTypeIdentifier = getEventLogTypeIdentifier(eventLogTypeItem);
        if (eventLogTypeIdentifier == null || eventLogTypeCollectionIdentifiers.includes(eventLogTypeIdentifier)) {
          return false;
        }
        eventLogTypeCollectionIdentifiers.push(eventLogTypeIdentifier);
        return true;
      });
      return [...eventLogTypesToAdd, ...eventLogTypeCollection];
    }
    return eventLogTypeCollection;
  }
}
