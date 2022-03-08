import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IEventLogType, EventLogType } from '../event-log-type.model';
import { EventLogTypeService } from '../service/event-log-type.service';
import { IEventLog } from 'app/entities/event-log/event-log.model';
import { EventLogService } from 'app/entities/event-log/service/event-log.service';

@Component({
  selector: 'jhi-event-log-type-update',
  templateUrl: './event-log-type-update.component.html',
})
export class EventLogTypeUpdateComponent implements OnInit {
  isSaving = false;

  eventLogsSharedCollection: IEventLog[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    template: [],
    eventLog: [],
  });

  constructor(
    protected eventLogTypeService: EventLogTypeService,
    protected eventLogService: EventLogService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ eventLogType }) => {
      this.updateForm(eventLogType);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const eventLogType = this.createFromForm();
    if (eventLogType.id !== undefined) {
      this.subscribeToSaveResponse(this.eventLogTypeService.update(eventLogType));
    } else {
      this.subscribeToSaveResponse(this.eventLogTypeService.create(eventLogType));
    }
  }

  trackEventLogById(index: number, item: IEventLog): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEventLogType>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(eventLogType: IEventLogType): void {
    this.editForm.patchValue({
      id: eventLogType.id,
      name: eventLogType.name,
      template: eventLogType.template,
      eventLog: eventLogType.eventLog,
    });

    this.eventLogsSharedCollection = this.eventLogService.addEventLogToCollectionIfMissing(
      this.eventLogsSharedCollection,
      eventLogType.eventLog
    );
  }

  protected loadRelationshipsOptions(): void {
    this.eventLogService
      .query()
      .pipe(map((res: HttpResponse<IEventLog[]>) => res.body ?? []))
      .pipe(
        map((eventLogs: IEventLog[]) =>
          this.eventLogService.addEventLogToCollectionIfMissing(eventLogs, this.editForm.get('eventLog')!.value)
        )
      )
      .subscribe((eventLogs: IEventLog[]) => (this.eventLogsSharedCollection = eventLogs));
  }

  protected createFromForm(): IEventLogType {
    return {
      ...new EventLogType(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      template: this.editForm.get(['template'])!.value,
      eventLog: this.editForm.get(['eventLog'])!.value,
    };
  }
}
