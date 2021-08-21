import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IExcelImport } from '../excel-import.model';
import { ExcelImportService } from '../service/excel-import.service';

@Component({
  templateUrl: './excel-import-delete-dialog.component.html',
})
export class ExcelImportDeleteDialogComponent {
  excelImport?: IExcelImport;

  constructor(protected excelImportService: ExcelImportService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.excelImportService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
