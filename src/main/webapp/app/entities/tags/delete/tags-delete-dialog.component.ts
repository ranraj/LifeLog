import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITags } from '../tags.model';
import { TagsService } from '../service/tags.service';

@Component({
  templateUrl: './tags-delete-dialog.component.html',
})
export class TagsDeleteDialogComponent {
  tags?: ITags;

  constructor(protected tagsService: TagsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tagsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
