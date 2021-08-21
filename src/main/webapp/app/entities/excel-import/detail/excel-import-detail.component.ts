import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IExcelImport } from '../excel-import.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-excel-import-detail',
  templateUrl: './excel-import-detail.component.html',
})
export class ExcelImportDetailComponent implements OnInit {
  excelImport: IExcelImport | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ excelImport }) => {
      this.excelImport = excelImport;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
