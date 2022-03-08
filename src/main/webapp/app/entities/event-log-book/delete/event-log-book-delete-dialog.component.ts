import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEventLogBook } from '../event-log-book.model';
import { EventLogBookService } from '../service/event-log-book.service';

@Component({
  templateUrl: './event-log-book-delete-dialog.component.html',
})
export class EventLogBookDeleteDialogComponent {
  eventLogBook?: IEventLogBook;

  constructor(protected eventLogBookService: EventLogBookService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.eventLogBookService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
