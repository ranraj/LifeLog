import { Component, OnInit,Input,Output, EventEmitter } from '@angular/core';
import { EventLogBook } from '../../event-log-book.model';

@Component({
  selector: 'jhi-book-side-bar',
  templateUrl: './book-side-bar.component.html',
  styleUrls: ['./book-side-bar.component.scss']
})
export class BookSideBarComponent implements OnInit {
  @Input() eventLogBooks : EventLogBook[] = [];
  @Output() setBookIdEvent = new EventEmitter<number>();
  bookId? : number;

  constructor() { }

  ngOnInit(): void {
  }

  onBookIdChange(bookId?: number){
    this.bookId = bookId;
    console.log("side bar",bookId);
    this.setBookIdEvent.emit(this.bookId);
  }

}
