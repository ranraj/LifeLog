import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { EventLog, IEventLog } from '../event-log.model';
import { EventLogService } from '../service/event-log.service';
import { EventLogDeleteDialogComponent } from '../delete/event-log-delete-dialog.component';
import { FormBuilder, NgForm } from '@angular/forms';
import { finalize, map, Observable } from 'rxjs';
import { IUser } from 'app/admin/user-management/user-management.model';
import { IEventLogBook } from 'app/entities/event-log-book/event-log-book.model';
import { ITags } from 'app/entities/tags/tags.model';

import { UserService } from 'app/entities/user/user.service';
import { EventLogBookService } from 'app/entities/event-log-book/service/event-log-book.service';
import { TagsService } from 'app/entities/tags/service/tags.service';

@Component({
  selector: 'jhi-event-log',
  templateUrl: './event-log.component.html',
})
export class EventLogComponent implements OnInit {
  eventLogs: IEventLog[] = [];
  isLoading = false;
  filterEventLogs = '';
  filteredAndSortedEventLogs: IEventLog[] = [];
  orderProp: keyof IEventLog = 'name';

  // For Edit Pop up

  eventLogRef?: IEventLog;
  @ViewChild('content') content?: TemplateRef<any>;
  isSaving?: boolean;

  // Services Content

  usersSharedCollection: IUser[] = [];
  tagsSharedCollection: ITags[] = [];
  eventLogBooksSharedCollection: IEventLogBook[] = [];

  constructor(
    protected eventLogService: EventLogService,
    protected modalService: NgbModal,
    protected fb: FormBuilder,
    protected userService: UserService,
    protected tagsService: TagsService,
    protected eventLogBookService: EventLogBookService
  ) {}

  loadAll(): void {
    this.isLoading = true;

    this.eventLogService.query().subscribe({
      next: (res: HttpResponse<IEventLog[]>) => {
        this.isLoading = false;
        this.eventLogs = res.body ?? [];
        this.filteredAndSortedEventLogs = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();

    this.loadRelationshipsOptions();
  }

  trackId(index: number, item: IEventLog): number {
    return item.id!;
  }

  delete(eventLog: IEventLog): void {
    const modalRef = this.modalService.open(EventLogDeleteDialogComponent, { size: 'xl', backdrop: 'static' });
    modalRef.componentInstance.eventLog = eventLog;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }

  // Filtering based on name

  filterAndSortEventLogs(): void {
    this.filteredAndSortedEventLogs = this.eventLogs
      .filter(eventLog => !this.filterEventLogs || eventLog.name?.toLowerCase().includes(this.filterEventLogs.toLowerCase()))
      .sort();
  }

  // Clear Button Function

  onClear(): void {
    this.filterEventLogs = '';
    this.filterAndSortEventLogs();
  }

  onEdit(eventLog: IEventLog): void {
    this.eventLogRef = eventLog;
    this.modalService.open(this.content);
  }

  onSubmit(editForm: NgForm): void {
    this.isSaving = true;

    const eventLog = this.createFromForm(editForm.value);

    if (eventLog.id !== undefined) {
      this.subscribeToSaveResponse(this.eventLogService.update(eventLog));
    } else {
      this.subscribeToSaveResponse(this.eventLogService.create(eventLog));
    }

    this.modalService.dismissAll();
  }

  // Tags

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

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.eventLogRef?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.tagsService
      .query()
      .pipe(map((res: HttpResponse<ITags[]>) => res.body ?? []))
      .pipe(map((tags: ITags[]) => this.tagsService.addTagsToCollectionIfMissing(tags, ...(this.eventLogRef?.tags ?? []))))
      .subscribe((tags: ITags[]) => (this.tagsSharedCollection = tags));

    this.eventLogBookService
      .query()
      .pipe(map((res: HttpResponse<IEventLogBook[]>) => res.body ?? []))
      .pipe(
        map((eventLogBooks: IEventLogBook[]) =>
          this.eventLogBookService.addEventLogBookToCollectionIfMissing(eventLogBooks, this.eventLogRef?.eventLogBook)
        )
      )
      .subscribe((eventLogBooks: IEventLogBook[]) => (this.eventLogBooksSharedCollection = eventLogBooks));
  }

  protected createFromForm(editForm: IEventLog): IEventLog {
    const selectedlog = this.eventLogs.find(logs => logs.uuid === editForm.uuid);
    return {
      ...new EventLog(),
      ...selectedlog,
      ...editForm,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEventLog>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: res => this.onSaveSuccess(res.body ?? {}),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected onSaveSuccess(res: IEventLog): void {
    const selectedLog = this.eventLogs.findIndex(logs => logs.uuid === res.uuid);

    if (selectedLog >= 0 && this.eventLogs[selectedLog]) {
      this.eventLogs[selectedLog] = res;
      this.filteredAndSortedEventLogs[selectedLog] = res;
    }
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }
}
