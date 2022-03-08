import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEventLogBook } from '../event-log-book.model';

@Component({
  selector: 'jhi-event-log-book-detail',
  templateUrl: './event-log-book-detail.component.html',
})
export class EventLogBookDetailComponent implements OnInit {
  eventLogBook: IEventLogBook | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ eventLogBook }) => {
      this.eventLogBook = eventLogBook;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
