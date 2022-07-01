import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SmsRoutingModule } from './route/sms-routing.module';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import {
  DxBulletModule,
  DxButtonModule,
  DxChartModule,
  DxDataGridModule,
  DxLoadPanelModule,
  DxSelectBoxModule,
  DxTemplateModule,
} from 'devextreme-angular';
import { NgxMatSelectSearchModule } from 'ngx-mat-select-search';
import { CommonModule } from '@angular/common';
import { DpDatePickerModule } from 'ng2-jalali-date-picker';
import { SmsUpdateComponent } from './update/sms-update.component';

@NgModule({
  imports: [
    SharedModule,
    SmsRoutingModule,
    CommonModule,
    MatSelectModule,
    MatFormFieldModule,
    MatIconModule,
    NgxMatSelectSearchModule,
    DxButtonModule,
    DpDatePickerModule,
    DxDataGridModule,
    DxTemplateModule,
    DxSelectBoxModule,
    DpDatePickerModule,
    DxLoadPanelModule,
    DxBulletModule,
    DxChartModule,
  ],
  declarations: [SmsUpdateComponent],
  entryComponents: [],
})
export class SmsModule {}
