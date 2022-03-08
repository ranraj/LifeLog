import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEventLog, getEventLogIdentifier } from '../event-log.model';

export type EntityResponseType = HttpResponse<IEventLog>;
export type EntityArrayResponseType = HttpResponse<IEventLog[]>;

@Injectable({ providedIn: 'root' })
export class EventLogService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/event-logs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(eventLog: IEventLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(eventLog);
    return this.http
      .post<IEventLog>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(eventLog: IEventLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(eventLog);
    return this.http
      .put<IEventLog>(`${this.resourceUrl}/${getEventLogIdentifier(eventLog) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(eventLog: IEventLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(eventLog);
    return this.http
      .patch<IEventLog>(`${this.resourceUrl}/${getEventLogIdentifier(eventLog) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEventLog>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEventLog[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEventLogToCollectionIfMissing(eventLogCollection: IEventLog[], ...eventLogsToCheck: (IEventLog | null | undefined)[]): IEventLog[] {
    const eventLogs: IEventLog[] = eventLogsToCheck.filter(isPresent);
    if (eventLogs.length > 0) {
      const eventLogCollectionIdentifiers = eventLogCollection.map(eventLogItem => getEventLogIdentifier(eventLogItem)!);
      const eventLogsToAdd = eventLogs.filter(eventLogItem => {
        const eventLogIdentifier = getEventLogIdentifier(eventLogItem);
        if (eventLogIdentifier == null || eventLogCollectionIdentifiers.includes(eventLogIdentifier)) {
          return false;
        }
        eventLogCollectionIdentifiers.push(eventLogIdentifier);
        return true;
      });
      return [...eventLogsToAdd, ...eventLogCollection];
    }
    return eventLogCollection;
  }

  protected convertDateFromClient(eventLog: IEventLog): IEventLog {
    return Object.assign({}, eventLog, {
      createdDate: eventLog.createdDate?.isValid() ? eventLog.createdDate.format(DATE_FORMAT) : undefined,
      updatedDate: eventLog.updatedDate?.isValid() ? eventLog.updatedDate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((eventLog: IEventLog) => {
        eventLog.createdDate = eventLog.createdDate ? dayjs(eventLog.createdDate) : undefined;
        eventLog.updatedDate = eventLog.updatedDate ? dayjs(eventLog.updatedDate) : undefined;
      });
    }
    return res;
  }
}
