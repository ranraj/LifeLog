import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EventLogBookComponent } from '../list/event-log-book.component';
import { EventLogBookDetailComponent } from '../detail/event-log-book-detail.component';
import { EventLogBookUpdateComponent } from '../update/event-log-book-update.component';
import { EventLogBookRoutingResolveService } from './event-log-book-routing-resolve.service';

const eventLogBookRoute: Routes = [
  {
    path: '',
    component: EventLogBookComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EventLogBookDetailComponent,
    resolve: {
      eventLogBook: EventLogBookRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EventLogBookUpdateComponent,
    resolve: {
      eventLogBook: EventLogBookRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EventLogBookUpdateComponent,
    resolve: {
      eventLogBook: EventLogBookRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(eventLogBookRoute)],
  exports: [RouterModule],
})
export class EventLogBookRoutingModule {}
