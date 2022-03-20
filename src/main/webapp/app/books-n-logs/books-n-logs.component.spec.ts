import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BooksNLogsComponent } from './books-n-logs.component';

describe('BooksNLogsComponent', () => {
  let component: BooksNLogsComponent;
  let fixture: ComponentFixture<BooksNLogsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BooksNLogsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BooksNLogsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
