import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EventLogTypeDetailComponent } from './event-log-type-detail.component';

describe('EventLogType Management Detail Component', () => {
  let comp: EventLogTypeDetailComponent;
  let fixture: ComponentFixture<EventLogTypeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EventLogTypeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ eventLogType: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EventLogTypeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EventLogTypeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load eventLogType on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.eventLogType).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
