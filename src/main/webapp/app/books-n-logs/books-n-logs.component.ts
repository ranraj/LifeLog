import { Component, OnInit } from '@angular/core';
import { NavigationEnd, Router, ActivatedRoute, ParamMap } from '@angular/router';
import { NIL } from 'uuid';
import { EventLogBook,IEventLogBook } from 'app/entities/event-log-book/event-log-book.model';
import { EventLogBookService } from 'app/entities/event-log-book/service/event-log-book.service';
import { HttpResponse } from '@angular/common/http';
import { CommonService, EventLogReloadRequest } from 'app/entities/event-log-book/service/event-log-pub-service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'jhi-books-n-logs',
  templateUrl: './books-n-logs.component.html',
  styleUrls: ['./books-n-logs.component.scss']
})

export class BooksNLogsComponent implements OnInit {
  private subscriptionName: Subscription;
  isLoading = false;
  eventLogBooks: IEventLogBook[] = [];
  eventID?: number;
  reloadRequest?: EventLogReloadRequest;

  ngOnInit(): void {
    this.loadAll();
  }

  constructor(
    protected router: Router,
    private route: ActivatedRoute,
    protected eventLogBookService: EventLogBookService,            
    protected commonService: CommonService,
  ) {
    this.subscriptionName= this.commonService.getUpdate().subscribe
    (message => { //message contains the data sent from service
        console.log('received',message);
        this.reloadRequest = message;
        this.eventID = message.id;
        this.loadAll();
    });
  }


  loadAll(): void {    
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

  bookIdEventHander($event: any) {
    console.log("book ",$event);
    this.reloadRequestToEventLog($event);
    this.eventID = $event;
  }
  reloadRequestToEventLog(id: number): void {
    // send message to subscribers via observable subject
    this.commonService.sendUpdate(id);
  }   
}
