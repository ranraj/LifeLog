import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EventLogBookDetailComponent } from './event-log-book-detail.component';

describe('EventLogBook Management Detail Component', () => {
  let comp: EventLogBookDetailComponent;
  let fixture: ComponentFixture<EventLogBookDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EventLogBookDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ eventLogBook: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EventLogBookDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EventLogBookDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load eventLogBook on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.eventLogBook).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
