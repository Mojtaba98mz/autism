import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable, ReplaySubject, Subject } from 'rxjs';
import { debounceTime, delay, filter, finalize, map, takeUntil, tap } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IDonation, Donation } from '../register-donation.model';
import { RegisterDonationService } from '../service/register-donation.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { Giver, IGiver } from 'app/entities/giver/giver.model';
import { GiverService } from 'app/entities/giver/service/giver.service';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { GiverModalService } from '../../../core/giver-selection/giver-modal.service';
import { GiverSelectionService } from '../../../core/giver-selection/giver-selection.service';

@Component({
  selector: 'jhi-donation-update',
  templateUrl: './register-donation-update.component.html',
  styleUrls: ['./register-donation-update.scss'],
})
export class RegisterDonationUpdateComponent implements OnInit {
  isSaving = false;

  public searching = false;
  giversSharedCollection: IGiver[] = [];
  giverError = false;
  private _selectedGiver: IGiver | undefined;

  editForm = this.fb.group({
    id: [],
    isCash: false,
    amount: [null, [Validators.required]],
    donationDate: [],
    helpType: [],
    description: [],
    receipt: [],
    receiptContentType: [],
    account: [],
    giver: [],
  });

  protected _onDestroy = new Subject<void>();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected donationService: RegisterDonationService,
    protected giverService: GiverService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected giverModalService: GiverModalService,
    protected giverSelectionService: GiverSelectionService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ donation }) => {
      if (donation === undefined || donation.id === undefined) {
        const today = dayjs().startOf('day');
        donation.givenDate = today;
      }

      this.updateForm(donation);

      this.loadRelationshipsOptions();
    });
  }

  showGivers(): void {
    const modalRef: NgbModalRef = this.giverModalService.open();
    modalRef.result.finally(() => (this.selectedGiver = this.giverSelectionService.giverSelected));
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
    const donation = this.createFromForm();
    this.subscribeToSaveResponse(this.donationService.create(donation));
  }

  trackGiverById(index: number, item: IGiver): number {
    return item.id!;
  }

  get selectedGiver(): IGiver | undefined {
    return this._selectedGiver;
  }

  set selectedGiver(value: IGiver | undefined) {
    this._selectedGiver = value;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDonation>>): void {
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

  protected updateForm(donation: IDonation): void {
    this.editForm.patchValue({
      id: donation.id,
      isCash: donation.isCash,
      amount: donation.amount,
      donationDate: donation.donationDate ? donation.donationDate.format(DATE_TIME_FORMAT) : null,
      helpType: donation.helpType,
      description: donation.description,
      receipt: donation.receipt,
      receiptContentType: donation.receiptContentType,
      account: donation.account,
    });

    this.giversSharedCollection = this.giverService.addGiverToCollectionIfMissing(this.giversSharedCollection, donation.giver);
  }

  protected loadRelationshipsOptions(): void {
    this.giverService
      .query()
      .pipe(map((res: HttpResponse<IGiver[]>) => res.body ?? []))
      .pipe(map((givers: IGiver[]) => this.giverService.addGiverToCollectionIfMissing(givers, this.editForm.get('giver')!.value)))
      .subscribe((givers: IGiver[]) => (this.giversSharedCollection = givers));
  }

  protected createFromForm(): IDonation {
    return {
      ...new Donation(),
      id: this.editForm.get(['id'])!.value,
      isCash: this.editForm.get(['isCash'])!.value,
      amount: this.editForm.get(['amount'])!.value,
      donationDate: this.editForm.get(['donationDate'])!.value
        ? dayjs(this.editForm.get(['donationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      helpType: this.editForm.get(['helpType'])!.value,
      description: this.editForm.get(['description'])!.value,
      receiptContentType: this.editForm.get(['receiptContentType'])!.value,
      receipt: this.editForm.get(['receipt'])!.value,
      account: this.editForm.get(['account'])!.value,
      giver: this.selectedGiver,
    };
  }
}
