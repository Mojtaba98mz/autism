import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IDonation, Donation } from '../donation.model';
import { DonationService } from '../service/donation.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { Giver, IGiver } from 'app/entities/giver/giver.model';
import { GiverService } from 'app/entities/giver/service/giver.service';
import { HelpType } from 'app/entities/enumerations/help-type.model';
import { Account } from 'app/entities/enumerations/account.model';
import * as moment from 'jalali-moment';

@Component({
  selector: 'jhi-donation-update',
  templateUrl: './donation-update.component.html',
})
export class DonationUpdateComponent implements OnInit {
  datePickerConfig = {
    format: 'jYYYY-jMM-jD',
  };
  isSaving = false;
  helpTypeValues = Object.keys(HelpType);
  accountValues = Object.keys(Account);

  giversSharedCollection: IGiver[] = [];

  editForm = this.fb.group({
    id: [],
    isCash: [],
    amount: [null, [Validators.required]],
    donationDate: [null, [Validators.required]],
    helpType: [],
    description: [],
    receipt: [],
    receiptContentType: [],
    account: [],
    giver: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected donationService: DonationService,
    protected giverService: GiverService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ donation }) => {
      if (donation.id === undefined) {
        const today = dayjs().startOf('day');
        // donation.donationDate = today;
        // donation.registerDate = today;
      }

      this.updateForm(donation);

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
    const donation = this.createFromForm();
    if (donation.id !== null && donation.id !== undefined) {
      this.subscribeToSaveResponse(this.donationService.update(donation));
    } else {
      this.subscribeToSaveResponse(this.donationService.create(donation));
    }
  }

  trackGiverById(index: number, item: IGiver): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDonation>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
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
    if (donation.id !== undefined) {
      const m = moment(
        moment(donation.donationDate?.format('YYYY-MM-DD'), 'YYYY/MM/DD').locale('fa').format('YYYY/MM/DD'),
        'jYYYY/jMM/jDD'
      );
      this.editForm.get(['donationDate'])!.setValue(m);
    }
    this.editForm.patchValue({
      id: donation.id,
      isCash: donation.isCash,
      amount: donation.amount,
      helpType: donation.helpType,
      description: donation.description,
      receipt: donation.receipt,
      receiptContentType: donation.receiptContentType,
      account: donation.account,
      giver: new Giver(this.activatedRoute.snapshot.params['giverId']),
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
    const jalaliDonationDate = this.editForm.get(['donationDate'])!.value;
    const gregorianDonationDate = moment.from(jalaliDonationDate.format('jYYYY-jMM-jD'), 'fa', 'YYYY-MM-DD').format('YYYY-MM-DD');
    return {
      ...new Donation(),
      id: this.editForm.get(['id'])!.value,
      isCash: this.editForm.get(['isCash'])!.value,
      amount: this.editForm.get(['amount'])!.value,
      donationDate: this.editForm.get(['donationDate'])!.value ? dayjs(gregorianDonationDate, DATE_FORMAT) : undefined,
      helpType: this.editForm.get(['helpType'])!.value,
      description: this.editForm.get(['description'])!.value,
      receiptContentType: this.editForm.get(['receiptContentType'])!.value,
      receipt: this.editForm.get(['receipt'])!.value,
      account: this.editForm.get(['account'])!.value,
      giver: new Giver(this.activatedRoute.snapshot.params['giverId']),
    };
  }
}
