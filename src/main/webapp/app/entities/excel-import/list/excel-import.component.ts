import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IExcelImport } from '../excel-import.model';
import { ExcelImportService } from '../service/excel-import.service';
import { ExcelImportDeleteDialogComponent } from '../delete/excel-import-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-excel-import',
  templateUrl: './excel-import.component.html',
})
export class ExcelImportComponent implements OnInit {
  excelImports?: IExcelImport[];
  isLoading = false;

  constructor(protected excelImportService: ExcelImportService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.excelImportService.query().subscribe(
      (res: HttpResponse<IExcelImport[]>) => {
        this.isLoading = false;
        this.excelImports = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IExcelImport): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(excelImport: IExcelImport): void {
    const modalRef = this.modalService.open(ExcelImportDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.excelImport = excelImport;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
