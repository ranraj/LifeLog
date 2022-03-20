import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { EventLogBook, IEventLogBook } from '../event-log-book.model';
import { EventLogBookService } from '../service/event-log-book.service';
import { CommonService } from '../service/event-log-pub-service';
import { EventLogBookDeleteDialogComponent } from '../delete/event-log-book-delete-dialog.component';
import { FormBuilder, NgForm } from '@angular/forms';
import { finalize } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { IEventLog } from 'app/entities/event-log/event-log.model';
import { NavigationEnd, Router, ActivatedRoute, ParamMap } from '@angular/router';
import { NIL } from 'uuid';

@Component({
  selector: 'jhi-event-log-book',
  templateUrl: './event-log-book.component.html',
  styleUrls: ['./event-log-book.component.css'],
})
export class EventLogBookComponent implements OnInit {
  eventLogBooks: IEventLogBook[] = [];
  isLoading = false;
  eventRef?: IEventLogBook;

  // Show Event-Log Details in Event-Log-Book -  Created by shan 17-03
  eventlogs?: IEventLog[] = [];
  eventID?: number;
  reload: any;

  @ViewChild('content') content?: TemplateRef<any>;
  httpClient: any;
  isSaving?: boolean;

  constructor(
    protected router: Router,
    private route: ActivatedRoute,
    protected eventLogBookService: EventLogBookService,
    protected modalService: NgbModal,
    protected fb: FormBuilder,
    protected service: CommonService,
  ) {}

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

  reloadRequestToEventLog(id: number): void {
    // send message to subscribers via observable subject
    this.service.sendUpdate(id);
  }

  ngOnInit(): void {
    let id = this.route.snapshot.paramMap.get('id');
    if(id){ this.eventID = parseInt(id);}
    console.log('eventId',this.eventID);
    this.loadAll();
  }

  bookIdEventHander($event: any) {
    console.log("book ",$event);
    this.reloadRequestToEventLog($event);
    this.eventID = $event;
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
    if (eventLogBook.id !== undefined) {
      this.subscribeToSaveResponse(this.eventLogBookService.update(eventLogBook));
    } else {
      this.subscribeToSaveResponse(this.eventLogBookService.create(eventLogBook));
    }
    this.modalService.dismissAll();
  }

  eventId(eventId: any): void {
    this.eventID = eventId;

    this.reload = this.router.events.subscribe(eventID => {
      if (eventID instanceof NavigationEnd) {
        this.router.navigated = false;
      }
    });
  }

  protected createFromForm(editForm: IEventLogBook): IEventLogBook {
    const book = this.eventLogBooks.find(books => books.uuid === editForm.uuid);
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
    const book = this.eventLogBooks.findIndex(books => books.uuid === res.uuid);
    if (book >= 0 && this.eventLogBooks[book]) {
      this.eventLogBooks[book] = res;
    }
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  // Show Event-Log Details in Event-Log-Book -  Created by shan 17-03
}
