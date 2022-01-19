import { Component, ElementRef, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { Ceremony, ICeremony } from '../register-ceremony.model';
import { RegisterCeremonyService } from '../service/register-ceremony.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { CeremonyUser, ICeremonyUser } from 'app/entities/ceremony-user/ceremony-user.model';
import { CeremonyUserService } from 'app/entities/ceremony-user/service/ceremony-user.service';

@Component({
  selector: 'jhi-ceremony-update',
  templateUrl: './register-ceremony-update.component.html',
  styleUrls: ['./register-ceremony-update.scss'],
})
export class RegisterCeremonyUpdateComponent implements OnInit {
  isSaving = false;

  ceremonyUsersSharedCollection: ICeremonyUser[] = [];

  editForm = this.fb.group({
    id: [],
    amount: [],
    givenDate: [],
    description: [],
    receipt: [],
    receiptContentType: [],
    ceremonyUser: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected ceremonyService: RegisterCeremonyService,
    protected ceremonyUserService: CeremonyUserService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ceremony }) => {
      if (ceremony.id === undefined) {
        const today = dayjs().startOf('day');
        ceremony.givenDate = today;
      }

      this.updateForm(ceremony);

      this.loadRelationshipsOptions();
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

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ceremony = this.createFromForm();
    if (ceremony.id !== undefined) {
      this.subscribeToSaveResponse(this.ceremonyService.update(ceremony));
    } else {
      this.subscribeToSaveResponse(this.ceremonyService.create(ceremony));
    }
  }

  trackCeremonyUserById(index: number, item: ICeremonyUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICeremony>>): void {
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

  protected updateForm(ceremony: ICeremony): void {
    this.editForm.patchValue({
      id: ceremony.id,
      amount: ceremony.amount,
      givenDate: ceremony.givenDate ? ceremony.givenDate.format(DATE_TIME_FORMAT) : null,
      description: ceremony.description,
      receipt: ceremony.receipt,
      receiptContentType: ceremony.receiptContentType,
      ceremonyUser: new CeremonyUser(this.activatedRoute.snapshot.params['ceremonyUserId']),
    });

    this.ceremonyUsersSharedCollection = this.ceremonyUserService.addCeremonyUserToCollectionIfMissing(
      this.ceremonyUsersSharedCollection,
      ceremony.ceremonyUser
    );
  }

  protected loadRelationshipsOptions(): void {
    this.ceremonyUserService
      .query()
      .pipe(map((res: HttpResponse<ICeremonyUser[]>) => res.body ?? []))
      .pipe(
        map((ceremonyUsers: ICeremonyUser[]) =>
          this.ceremonyUserService.addCeremonyUserToCollectionIfMissing(ceremonyUsers, this.editForm.get('ceremonyUser')!.value)
        )
      )
      .subscribe((ceremonyUsers: ICeremonyUser[]) => (this.ceremonyUsersSharedCollection = ceremonyUsers));
  }

  protected createFromForm(): ICeremony {
    return {
      ...new Ceremony(),
      id: this.editForm.get(['id'])!.value,
      amount: this.editForm.get(['amount'])!.value,
      givenDate: this.editForm.get(['givenDate'])!.value ? dayjs(this.editForm.get(['givenDate'])!.value, DATE_TIME_FORMAT) : undefined,
      description: this.editForm.get(['description'])!.value,
      receiptContentType: this.editForm.get(['receiptContentType'])!.value,
      receipt: this.editForm.get(['receipt'])!.value,
      ceremonyUser: this.editForm.get(['ceremonyUser'])!.value,
    };
  }
}
