import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EventLogBookComponent } from './list/event-log-book.component';
import { EventLogBookDetailComponent } from './detail/event-log-book-detail.component';
import { EventLogBookUpdateComponent } from './update/event-log-book-update.component';
import { EventLogBookDeleteDialogComponent } from './delete/event-log-book-delete-dialog.component';
import { EventLogBookRoutingModule } from './route/event-log-book-routing.module';
import { MatExpansionModule } from '@angular/material/expansion';
import { EventLogComponent } from '../event-log/list/event-log.component';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
@NgModule({
  imports: [SharedModule, EventLogBookRoutingModule, MatExpansionModule, MatDatepickerModule, MatFormFieldModule],
  declarations: [
    EventLogBookComponent,
    EventLogBookDetailComponent,
    EventLogBookUpdateComponent,
    EventLogBookDeleteDialogComponent,
    EventLogComponent,
  ],
  entryComponents: [EventLogBookDeleteDialogComponent],
})
export class EventLogBookModule {}
