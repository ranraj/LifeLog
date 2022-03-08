import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { EventLogBookService } from '../service/event-log-book.service';

import { EventLogBookComponent } from './event-log-book.component';

describe('EventLogBook Management Component', () => {
  let comp: EventLogBookComponent;
  let fixture: ComponentFixture<EventLogBookComponent>;
  let service: EventLogBookService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [EventLogBookComponent],
    })
      .overrideTemplate(EventLogBookComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EventLogBookComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(EventLogBookService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.eventLogBooks?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
