import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITags, Tags } from '../tags.model';
import { TagsService } from '../service/tags.service';

@Component({
  selector: 'jhi-tags-update',
  templateUrl: './tags-update.component.html',
})
export class TagsUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    description: [],
  });

  constructor(protected tagsService: TagsService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tags }) => {
      this.updateForm(tags);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tags = this.createFromForm();
    if (tags.id !== undefined) {
      this.subscribeToSaveResponse(this.tagsService.update(tags));
    } else {
      this.subscribeToSaveResponse(this.tagsService.create(tags));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITags>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(tags: ITags): void {
    this.editForm.patchValue({
      id: tags.id,
      name: tags.name,
      description: tags.description,
    });
  }

  protected createFromForm(): ITags {
    return {
      ...new Tags(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
    };
  }
}
