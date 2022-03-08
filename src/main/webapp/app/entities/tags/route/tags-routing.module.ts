import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TagsComponent } from '../list/tags.component';
import { TagsDetailComponent } from '../detail/tags-detail.component';
import { TagsUpdateComponent } from '../update/tags-update.component';
import { TagsRoutingResolveService } from './tags-routing-resolve.service';

const tagsRoute: Routes = [
  {
    path: '',
    component: TagsComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TagsDetailComponent,
    resolve: {
      tags: TagsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TagsUpdateComponent,
    resolve: {
      tags: TagsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TagsUpdateComponent,
    resolve: {
      tags: TagsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tagsRoute)],
  exports: [RouterModule],
})
export class TagsRoutingModule {}
