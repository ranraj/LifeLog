import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EventLogBookComponent } from './list/event-log-book.component';
import { EventLogBookDetailComponent } from './detail/event-log-book-detail.component';
import { EventLogBookUpdateComponent } from './update/event-log-book-update.component';
import { EventLogBookDeleteDialogComponent } from './delete/event-log-book-delete-dialog.component';
import { EventLogBookRoutingModule } from './route/event-log-book-routing.module';

@NgModule({
  imports: [SharedModule, EventLogBookRoutingModule],
  declarations: [EventLogBookComponent, EventLogBookDetailComponent, EventLogBookUpdateComponent, EventLogBookDeleteDialogComponent],
  entryComponents: [EventLogBookDeleteDialogComponent],
})
export class EventLogBookModule {}
