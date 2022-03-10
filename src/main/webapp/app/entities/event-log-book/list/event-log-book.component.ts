import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { EventLogBook, IEventLogBook } from '../event-log-book.model';
import { EventLogBookService } from '../service/event-log-book.service';
import { EventLogBookDeleteDialogComponent } from '../delete/event-log-book-delete-dialog.component';
import { FormBuilder, NgForm } from '@angular/forms';
import { finalize } from 'rxjs/operators';
import { Observable } from 'rxjs';

@Component({
  selector: 'jhi-event-log-book',
  templateUrl: './event-log-book.component.html',
})
export class EventLogBookComponent implements OnInit {
  eventLogBooks?: IEventLogBook[];
  isLoading = false;
  eventRef?: IEventLogBook;

  @ViewChild('content') content?: TemplateRef<any>;
  httpClient: any;
  isSaving?: boolean;

  constructor(protected eventLogBookService: EventLogBookService, protected modalService: NgbModal, protected fb: FormBuilder) {}

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

  onEdit(eventLogBook: IEventLogBook): void {
    this.eventRef = eventLogBook;
    this.modalService.open(this.content);
  }

  onSubmit(editForm: NgForm): void {
    this.isSaving = true;
    const eventLogBook = this.createFromForm(editForm.value);
    console.log(eventLogBook);
    if (eventLogBook.id !== undefined) {
      this.subscribeToSaveResponse(this.eventLogBookService.update(eventLogBook));
    } else {
      this.subscribeToSaveResponse(this.eventLogBookService.create(eventLogBook));
    }
    this.modalService.dismissAll();
  }

  protected createFromForm(editForm: IEventLogBook): IEventLogBook {
    const book = this.eventLogBooks?.find(books => books.uuid === editForm.uuid);
    return {
      ...new EventLogBook(),
      ...book,
      ...editForm,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEventLogBook>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: res => this.onSaveSuccess(res.body ?? {}),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected onSaveSuccess(res: IEventLogBook): void {
    const book = this.eventLogBooks?.findIndex(books => books.uuid === res.uuid);
    console.log(book);
    if (this.eventLogBooks && book !== undefined && book >= 0 && this.eventLogBooks[book]) {
      console.log('onsucess', res);
      this.eventLogBooks[book] = res;
    }
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }
}
