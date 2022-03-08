import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EventLogTypeComponent } from './list/event-log-type.component';
import { EventLogTypeDetailComponent } from './detail/event-log-type-detail.component';
import { EventLogTypeUpdateComponent } from './update/event-log-type-update.component';
import { EventLogTypeDeleteDialogComponent } from './delete/event-log-type-delete-dialog.component';
import { EventLogTypeRoutingModule } from './route/event-log-type-routing.module';

@NgModule({
  imports: [SharedModule, EventLogTypeRoutingModule],
  declarations: [EventLogTypeComponent, EventLogTypeDetailComponent, EventLogTypeUpdateComponent, EventLogTypeDeleteDialogComponent],
  entryComponents: [EventLogTypeDeleteDialogComponent],
})
export class EventLogTypeModule {}
