import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IExcelImport, ExcelImport } from '../excel-import.model';
import { ExcelImportService } from '../service/excel-import.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-excel-import-update',
  templateUrl: './excel-import-update.component.html',
})
export class ExcelImportUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    excel: [null, [Validators.required]],
    excelContentType: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected excelImportService: ExcelImportService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ excelImport }) => {
      this.updateForm(excelImport);
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('autismApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const excelImport = this.createFromForm();
    if (excelImport.id !== undefined) {
      this.subscribeToSaveResponse(this.excelImportService.update(excelImport));
    } else {
      this.subscribeToSaveResponse(this.excelImportService.create(excelImport));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExcelImport>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(excelImport: IExcelImport): void {
    this.editForm.patchValue({
      id: excelImport.id,
      excel: excelImport.excel,
      excelContentType: excelImport.excelContentType,
    });
  }

  protected createFromForm(): IExcelImport {
    return {
      ...new ExcelImport(),
      id: this.editForm.get(['id'])!.value,
      excelContentType: this.editForm.get(['excelContentType'])!.value,
      excel: this.editForm.get(['excel'])!.value,
    };
  }
}
