import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITags } from '../tags.model';
import { TagsService } from '../service/tags.service';
import { TagsDeleteDialogComponent } from '../delete/tags-delete-dialog.component';

@Component({
  selector: 'jhi-tags',
  templateUrl: './tags.component.html',
})
export class TagsComponent implements OnInit {
  tags?: ITags[];
  isLoading = false;

  constructor(protected tagsService: TagsService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.tagsService.query().subscribe({
      next: (res: HttpResponse<ITags[]>) => {
        this.isLoading = false;
        this.tags = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ITags): number {
    return item.id!;
  }

  delete(tags: ITags): void {
    const modalRef = this.modalService.open(TagsDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.tags = tags;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
