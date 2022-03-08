import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEventLog } from '../event-log.model';
import { EventLogService } from '../service/event-log.service';
import { EventLogDeleteDialogComponent } from '../delete/event-log-delete-dialog.component';

@Component({
  selector: 'jhi-event-log',
  templateUrl: './event-log.component.html',
})
export class EventLogComponent implements OnInit {
  eventLogs?: IEventLog[];
  isLoading = false;

  constructor(protected eventLogService: EventLogService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.eventLogService.query().subscribe({
      next: (res: HttpResponse<IEventLog[]>) => {
        this.isLoading = false;
        this.eventLogs = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IEventLog): number {
    return item.id!;
  }

  delete(eventLog: IEventLog): void {
    const modalRef = this.modalService.open(EventLogDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.eventLog = eventLog;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
