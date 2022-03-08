import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { TagsService } from '../service/tags.service';

import { TagsComponent } from './tags.component';

describe('Tags Management Component', () => {
  let comp: TagsComponent;
  let fixture: ComponentFixture<TagsComponent>;
  let service: TagsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TagsComponent],
    })
      .overrideTemplate(TagsComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TagsComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(TagsService);

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
    expect(comp.tags?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
