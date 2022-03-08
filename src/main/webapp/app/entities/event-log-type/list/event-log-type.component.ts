import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEventLogType } from '../event-log-type.model';
import { EventLogTypeService } from '../service/event-log-type.service';
import { EventLogTypeDeleteDialogComponent } from '../delete/event-log-type-delete-dialog.component';

@Component({
  selector: 'jhi-event-log-type',
  templateUrl: './event-log-type.component.html',
})
export class EventLogTypeComponent implements OnInit {
  eventLogTypes?: IEventLogType[];
  isLoading = false;

  constructor(protected eventLogTypeService: EventLogTypeService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.eventLogTypeService.query().subscribe({
      next: (res: HttpResponse<IEventLogType[]>) => {
        this.isLoading = false;
        this.eventLogTypes = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IEventLogType): number {
    return item.id!;
  }

  delete(eventLogType: IEventLogType): void {
    const modalRef = this.modalService.open(EventLogTypeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.eventLogType = eventLogType;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
