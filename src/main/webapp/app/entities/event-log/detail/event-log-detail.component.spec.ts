import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EventLogDetailComponent } from './event-log-detail.component';

describe('EventLog Management Detail Component', () => {
  let comp: EventLogDetailComponent;
  let fixture: ComponentFixture<EventLogDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EventLogDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ eventLog: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EventLogDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EventLogDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load eventLog on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.eventLog).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
