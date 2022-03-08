import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEventLog } from '../event-log.model';

@Component({
  selector: 'jhi-event-log-detail',
  templateUrl: './event-log-detail.component.html',
})
export class EventLogDetailComponent implements OnInit {
  eventLog: IEventLog | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ eventLog }) => {
      this.eventLog = eventLog;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
