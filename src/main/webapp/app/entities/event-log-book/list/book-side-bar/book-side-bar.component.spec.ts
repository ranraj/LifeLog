import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BookSideBarComponent } from './book-side-bar.component';

describe('BookSideBarComponent', () => {
  let component: BookSideBarComponent;
  let fixture: ComponentFixture<BookSideBarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BookSideBarComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BookSideBarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
