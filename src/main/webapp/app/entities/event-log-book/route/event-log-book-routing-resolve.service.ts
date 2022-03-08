import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEventLogBook, EventLogBook } from '../event-log-book.model';
import { EventLogBookService } from '../service/event-log-book.service';

@Injectable({ providedIn: 'root' })
export class EventLogBookRoutingResolveService implements Resolve<IEventLogBook> {
  constructor(protected service: EventLogBookService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEventLogBook> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((eventLogBook: HttpResponse<EventLogBook>) => {
          if (eventLogBook.body) {
            return of(eventLogBook.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new EventLogBook());
  }
}
