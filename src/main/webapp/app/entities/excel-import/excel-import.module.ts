import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ExcelImportComponent } from './list/excel-import.component';
import { ExcelImportDetailComponent } from './detail/excel-import-detail.component';
import { ExcelImportUpdateComponent } from './update/excel-import-update.component';
import { ExcelImportDeleteDialogComponent } from './delete/excel-import-delete-dialog.component';
import { ExcelImportRoutingModule } from './route/excel-import-routing.module';

@NgModule({
  imports: [SharedModule, ExcelImportRoutingModule],
  declarations: [ExcelImportComponent, ExcelImportDetailComponent, ExcelImportUpdateComponent, ExcelImportDeleteDialogComponent],
  entryComponents: [ExcelImportDeleteDialogComponent],
})
export class ExcelImportModule {}
