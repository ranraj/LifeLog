import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITags } from '../tags.model';

@Component({
  selector: 'jhi-tags-detail',
  templateUrl: './tags-detail.component.html',
})
export class TagsDetailComponent implements OnInit {
  tags: ITags | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tags }) => {
      this.tags = tags;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
