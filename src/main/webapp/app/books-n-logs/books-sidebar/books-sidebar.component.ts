import { Component, OnInit,Input,Output, EventEmitter } from '@angular/core';
import { EventLogBook } from 'app/entities/event-log-book/event-log-book.model';

@Component({
  selector: 'jhi-books-sidebar',
  templateUrl: './books-sidebar.component.html',
  styleUrls: ['./books-sidebar.component.scss']
})
export class BooksSidebarComponent implements OnInit {

  @Input() eventLogBooks : EventLogBook[] = [];
  @Output() setBookIdEvent = new EventEmitter<number>();

  bookId? : number;

  constructor() { }

  ngOnInit(): void {
  }

  onBookIdChange(bookId?: number){
    this.bookId = bookId;    
    this.setBookIdEvent.emit(this.bookId);
  }
}
