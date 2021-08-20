import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ExcelImportComponent } from './list/excel-import.component';
import { ExcelImportRoutingModule } from './route/excel-import-routing.module';

@NgModule({
  imports: [SharedModule, ExcelImportRoutingModule],
  declarations: [ExcelImportComponent],
})
export class ExcelImportModule {}
