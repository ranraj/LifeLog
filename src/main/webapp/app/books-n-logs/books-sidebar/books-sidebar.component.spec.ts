import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BooksSidebarComponent } from './books-sidebar.component';

describe('BooksSidebarComponent', () => {
  let component: BooksSidebarComponent;
  let fixture: ComponentFixture<BooksSidebarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BooksSidebarComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BooksSidebarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
