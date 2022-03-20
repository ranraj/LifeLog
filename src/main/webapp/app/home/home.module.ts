import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';
import { BooksNLogsModule } from 'app/books-n-logs/books-n-logs.module';
import { BooksNLogsComponent } from 'app/books-n-logs/books-n-logs.component';
import { BooksSidebarComponent } from 'app/books-n-logs/books-sidebar/books-sidebar.component';
import { LogsViewComponent } from 'app/books-n-logs/logs-view/logs-view.component';
@NgModule({
  imports: [SharedModule, RouterModule.forChild([HOME_ROUTE]),BooksNLogsModule],
  declarations: [HomeComponent,BooksNLogsComponent,BooksSidebarComponent,LogsViewComponent],
})
export class HomeModule {}
