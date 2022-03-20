import { Component, OnInit } from '@angular/core';
import { NavigationEnd, Router, ActivatedRoute, ParamMap } from '@angular/router';
import { NIL } from 'uuid';
import { EventLogBook,IEventLogBook } from 'app/entities/event-log-book/event-log-book.model';
import { EventLogBookService } from 'app/entities/event-log-book/service/event-log-book.service';
import { HttpResponse } from '@angular/common/http';
import { CommonService } from 'app/entities/event-log-book/service/event-log-pub-service';

@Component({
  selector: 'jhi-books-n-logs',
  templateUrl: './books-n-logs.component.html',
  styleUrls: ['./books-n-logs.component.scss']
})

export class BooksNLogsComponent implements OnInit {
  isLoading = false;
  eventLogBooks: IEventLogBook[] = [];
  eventID?: number;

  ngOnInit(): void {
    this.loadAll();
  }

  constructor(
    protected router: Router,
    private route: ActivatedRoute,
    protected eventLogBookService: EventLogBookService,            
    protected service: CommonService,
  ) {}


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
    this.service.sendUpdate(id);
  }

}
