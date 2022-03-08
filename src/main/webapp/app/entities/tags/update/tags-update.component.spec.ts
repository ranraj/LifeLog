import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TagsService } from '../service/tags.service';
import { ITags, Tags } from '../tags.model';

import { TagsUpdateComponent } from './tags-update.component';

describe('Tags Management Update Component', () => {
  let comp: TagsUpdateComponent;
  let fixture: ComponentFixture<TagsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tagsService: TagsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TagsUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(TagsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TagsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tagsService = TestBed.inject(TagsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const tags: ITags = { id: 456 };

      activatedRoute.data = of({ tags });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(tags));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Tags>>();
      const tags = { id: 123 };
      jest.spyOn(tagsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tags });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tags }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(tagsService.update).toHaveBeenCalledWith(tags);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Tags>>();
      const tags = new Tags();
      jest.spyOn(tagsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tags });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tags }));
      saveSubject.complete();

      // THEN
      expect(tagsService.create).toHaveBeenCalledWith(tags);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Tags>>();
      const tags = { id: 123 };
      jest.spyOn(tagsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tags });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tagsService.update).toHaveBeenCalledWith(tags);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
