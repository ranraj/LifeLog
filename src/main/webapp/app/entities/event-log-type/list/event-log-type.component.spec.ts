import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { EventLogTypeService } from '../service/event-log-type.service';

import { EventLogTypeComponent } from './event-log-type.component';

describe('EventLogType Management Component', () => {
  let comp: EventLogTypeComponent;
  let fixture: ComponentFixture<EventLogTypeComponent>;
  let service: EventLogTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [EventLogTypeComponent],
    })
      .overrideTemplate(EventLogTypeComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EventLogTypeComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(EventLogTypeService);

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
    expect(comp.eventLogTypes?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
