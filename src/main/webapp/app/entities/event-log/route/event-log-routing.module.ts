import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EventLogComponent } from '../list/event-log.component';
import { EventLogDetailComponent } from '../detail/event-log-detail.component';
import { EventLogUpdateComponent } from '../update/event-log-update.component';
import { EventLogRoutingResolveService } from './event-log-routing-resolve.service';

const eventLogRoute: Routes = [
  {
    path: '',
    component: EventLogComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EventLogDetailComponent,
    resolve: {
      eventLog: EventLogRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EventLogUpdateComponent,
    resolve: {
      eventLog: EventLogRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EventLogUpdateComponent,
    resolve: {
      eventLog: EventLogRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(eventLogRoute)],
  exports: [RouterModule],
})
export class EventLogRoutingModule {}
