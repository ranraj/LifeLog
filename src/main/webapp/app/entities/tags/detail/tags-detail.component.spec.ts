import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TagsDetailComponent } from './tags-detail.component';

describe('Tags Management Detail Component', () => {
  let comp: TagsDetailComponent;
  let fixture: ComponentFixture<TagsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TagsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ tags: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TagsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TagsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load tags on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.tags).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
