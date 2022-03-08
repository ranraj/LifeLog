import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'event-log-book',
        data: { pageTitle: 'lifeLogApp.eventLogBook.home.title' },
        loadChildren: () => import('./event-log-book/event-log-book.module').then(m => m.EventLogBookModule),
      },
      {
        path: 'event-log',
        data: { pageTitle: 'lifeLogApp.eventLog.home.title' },
        loadChildren: () => import('./event-log/event-log.module').then(m => m.EventLogModule),
      },
      {
        path: 'tags',
        data: { pageTitle: 'lifeLogApp.tags.home.title' },
        loadChildren: () => import('./tags/tags.module').then(m => m.TagsModule),
      },
      {
        path: 'event-log-type',
        data: { pageTitle: 'lifeLogApp.eventLogType.home.title' },
        loadChildren: () => import('./event-log-type/event-log-type.module').then(m => m.EventLogTypeModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
