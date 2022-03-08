import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEventLogBook } from '../event-log-book.model';
import { EventLogBookService } from '../service/event-log-book.service';
import { EventLogBookDeleteDialogComponent } from '../delete/event-log-book-delete-dialog.component';

@Component({
  selector: 'jhi-event-log-book',
  templateUrl: './event-log-book.component.html',
})
export class EventLogBookComponent implements OnInit {
  eventLogBooks?: IEventLogBook[];
  isLoading = false;

  constructor(protected eventLogBookService: EventLogBookService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.eventLogBookService.query().subscribe({
      next: (res: HttpResponse<IEventLogBook[]>) => {
        this.isLoading = false;
        this.eventLogBooks = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IEventLogBook): number {
    return item.id!;
  }

  delete(eventLogBook: IEventLogBook): void {
    const modalRef = this.modalService.open(EventLogBookDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.eventLogBook = eventLogBook;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
