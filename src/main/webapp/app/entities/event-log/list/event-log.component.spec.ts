import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { EventLogService } from '../service/event-log.service';

import { EventLogComponent } from './event-log.component';

describe('EventLog Management Component', () => {
  let comp: EventLogComponent;
  let fixture: ComponentFixture<EventLogComponent>;
  let service: EventLogService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [EventLogComponent],
    })
      .overrideTemplate(EventLogComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EventLogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(EventLogService);

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
    expect(comp.eventLogs?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
