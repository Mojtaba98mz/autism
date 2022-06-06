import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ReportUpdateComponent } from './update/report-update.component';
import { ReportRoutingModule } from './route/report-routing.module';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { DxButtonModule } from 'devextreme-angular';
import { NgxMatSelectSearchModule } from 'ngx-mat-select-search';
import { CommonModule } from '@angular/common';
import { DpDatePickerModule } from 'ng2-jalali-date-picker';

@NgModule({
  imports: [
    SharedModule,
    ReportRoutingModule,
    CommonModule,
    MatSelectModule,
    MatFormFieldModule,
    MatIconModule,
    NgxMatSelectSearchModule,
    DxButtonModule,
    DpDatePickerModule,
  ],
  declarations: [ReportUpdateComponent],
  entryComponents: [],
})
export class ReportModule {}
