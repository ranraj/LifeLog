import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEventLogType } from '../event-log-type.model';

@Component({
  selector: 'jhi-event-log-type-detail',
  templateUrl: './event-log-type-detail.component.html',
})
export class EventLogTypeDetailComponent implements OnInit {
  eventLogType: IEventLogType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ eventLogType }) => {
      this.eventLogType = eventLogType;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
