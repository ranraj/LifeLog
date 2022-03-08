import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEventLogType, EventLogType } from '../event-log-type.model';
import { EventLogTypeService } from '../service/event-log-type.service';

@Injectable({ providedIn: 'root' })
export class EventLogTypeRoutingResolveService implements Resolve<IEventLogType> {
  constructor(protected service: EventLogTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEventLogType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((eventLogType: HttpResponse<EventLogType>) => {
          if (eventLogType.body) {
            return of(eventLogType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new EventLogType());
  }
}
