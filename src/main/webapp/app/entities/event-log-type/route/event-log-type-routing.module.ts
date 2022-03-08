import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EventLogTypeComponent } from '../list/event-log-type.component';
import { EventLogTypeDetailComponent } from '../detail/event-log-type-detail.component';
import { EventLogTypeUpdateComponent } from '../update/event-log-type-update.component';
import { EventLogTypeRoutingResolveService } from './event-log-type-routing-resolve.service';

const eventLogTypeRoute: Routes = [
  {
    path: '',
    component: EventLogTypeComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EventLogTypeDetailComponent,
    resolve: {
      eventLogType: EventLogTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EventLogTypeUpdateComponent,
    resolve: {
      eventLogType: EventLogTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EventLogTypeUpdateComponent,
    resolve: {
      eventLogType: EventLogTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(eventLogTypeRoute)],
  exports: [RouterModule],
})
export class EventLogTypeRoutingModule {}
