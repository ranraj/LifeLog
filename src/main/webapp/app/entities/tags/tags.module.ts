import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TagsComponent } from './list/tags.component';
import { TagsDetailComponent } from './detail/tags-detail.component';
import { TagsUpdateComponent } from './update/tags-update.component';
import { TagsDeleteDialogComponent } from './delete/tags-delete-dialog.component';
import { TagsRoutingModule } from './route/tags-routing.module';

@NgModule({
  imports: [SharedModule, TagsRoutingModule],
  declarations: [TagsComponent, TagsDetailComponent, TagsUpdateComponent, TagsDeleteDialogComponent],
  entryComponents: [TagsDeleteDialogComponent],
})
export class TagsModule {}
