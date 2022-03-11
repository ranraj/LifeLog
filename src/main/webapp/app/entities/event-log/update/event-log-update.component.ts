import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IEventLog, EventLog } from '../event-log.model';
import { EventLogService } from '../service/event-log.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ITags } from 'app/entities/tags/tags.model';
import { TagsService } from 'app/entities/tags/service/tags.service';
import { IEventLogBook } from 'app/entities/event-log-book/event-log-book.model';
import { EventLogBookService } from 'app/entities/event-log-book/service/event-log-book.service';
import dayjs from 'dayjs/esm';

@Component({
  selector: 'jhi-event-log-update',
  templateUrl: './event-log-update.component.html',
})
export class EventLogUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  tagsSharedCollection: ITags[] = [];
  eventLogBooksSharedCollection: IEventLogBook[] = [];

  editForm = this.fb.group({
    id: [],
    uuid: [],
    name: [],
    detail: [null, [Validators.required]],
    createdDate: [],
    updatedDate: [],
    user: [],
    tags: [],
    eventLogBook: [],
  });

  constructor(
    protected eventLogService: EventLogService,
    protected userService: UserService,
    protected tagsService: TagsService,
    protected eventLogBookService: EventLogBookService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ eventLog }) => {
      this.updateForm(eventLog);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const eventLog = this.createFromForm();
    if (eventLog.id !== undefined) {
      this.subscribeToSaveResponse(this.eventLogService.update(eventLog));
    } else {
      this.subscribeToSaveResponse(this.eventLogService.create(eventLog));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackTagsById(index: number, item: ITags): number {
    return item.id!;
  }

  trackEventLogBookById(index: number, item: IEventLogBook): number {
    return item.id!;
  }

  getSelectedTags(option: ITags, selectedVals?: ITags[]): ITags {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEventLog>>): void {
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

  protected updateForm(eventLog: IEventLog): void {
    this.editForm.patchValue({
      id: eventLog.id,
      uuid: eventLog.uuid,
      name: eventLog.name,
      detail: eventLog.detail,
      createdDate: eventLog.createdDate,
      updatedDate: eventLog.updatedDate,
      user: eventLog.user,
      tags: eventLog.tags,
      eventLogBook: eventLog.eventLogBook,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, eventLog.user);
    this.tagsSharedCollection = this.tagsService.addTagsToCollectionIfMissing(this.tagsSharedCollection, ...(eventLog.tags ?? []));
    this.eventLogBooksSharedCollection = this.eventLogBookService.addEventLogBookToCollectionIfMissing(
      this.eventLogBooksSharedCollection,
      eventLog.eventLogBook
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.tagsService
      .query()
      .pipe(map((res: HttpResponse<ITags[]>) => res.body ?? []))
      .pipe(map((tags: ITags[]) => this.tagsService.addTagsToCollectionIfMissing(tags, ...(this.editForm.get('tags')!.value ?? []))))
      .subscribe((tags: ITags[]) => (this.tagsSharedCollection = tags));

    this.eventLogBookService
      .query()
      .pipe(map((res: HttpResponse<IEventLogBook[]>) => res.body ?? []))
      .pipe(
        map((eventLogBooks: IEventLogBook[]) =>
          this.eventLogBookService.addEventLogBookToCollectionIfMissing(eventLogBooks, this.editForm.get('eventLogBook')!.value)
        )
      )
      .subscribe((eventLogBooks: IEventLogBook[]) => (this.eventLogBooksSharedCollection = eventLogBooks));
  }

  protected createFromForm(): IEventLog {
    const date = new Date();
    return {
      ...new EventLog(),
      id: this.editForm.get(['id'])!.value,
      uuid: this.editForm.get(['uuid'])!.value,
      name: this.editForm.get(['name'])!.value,
      detail: this.editForm.get(['detail'])!.value,
      createdDate: this.editForm.get(['createdDate'])!.value ? this.editForm.get(['createdDate'])!.value : dayjs(date),
      updatedDate: dayjs(date),
      user: this.editForm.get(['user'])!.value,
      tags: this.editForm.get(['tags'])!.value,
      eventLogBook: this.editForm.get(['eventLogBook'])!.value,
    };
  }
}
