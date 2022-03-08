import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEventLogType } from '../event-log-type.model';
import { EventLogTypeService } from '../service/event-log-type.service';

@Component({
  templateUrl: './event-log-type-delete-dialog.component.html',
})
export class EventLogTypeDeleteDialogComponent {
  eventLogType?: IEventLogType;

  constructor(protected eventLogTypeService: EventLogTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.eventLogTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
