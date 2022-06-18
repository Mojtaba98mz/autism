import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable, ReplaySubject, Subject } from 'rxjs';
import { debounceTime, delay, filter, finalize, map, takeUntil, tap } from 'rxjs/operators';
import { DpDatePickerModule } from 'ng2-jalali-date-picker';

import * as dayjs from 'dayjs';
import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IReport, Report } from '../report.model';
import { ReportService } from '../service/report.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { Giver, IGiver } from 'app/entities/giver/giver.model';
import { GiverService } from 'app/entities/giver/service/giver.service';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { GiverModalService } from '../../../core/giver-selection/giver-modal.service';
import { GiverSelectionService } from '../../../core/giver-selection/giver-selection.service';
import * as moment from 'jalali-moment';
import { IProvince } from '../../province/province.model';
import { ICity } from '../../city/city.model';
import { ProvinceService } from '../../province/service/province.service';
import { CityService } from '../../city/service/city.service';
import { IUser } from '../../user/user.model';

@Component({
  selector: 'jhi-donation-update',
  templateUrl: './report-update.component.html',
  styleUrls: ['./report-update.scss'],
})
export class ReportUpdateComponent implements OnInit {
  isSaving = false;

  provincesCollection: IProvince[] = [];
  citiesCollection: ICity[] = [];

  public searching = false;
  giversSharedCollection: IGiver[] = [];
  giverError = false;
  private _selectedGiver: IGiver | undefined;

  editForm = this.fb.group({
    id: [],
    isCash: false,
    amountFrom: [],
    amountTo: [],
    fromDate: [],
    toDate: [],
    helpType: [],
    province: [],
    city: [],
  });

  protected _onDestroy = new Subject<void>();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected reportService: ReportService,
    protected giverService: GiverService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected giverModalService: GiverModalService,
    protected provinceService: ProvinceService,
    protected cityService: CityService,
    protected giverSelectionService: GiverSelectionService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ donation }) => {
      if (donation === undefined || donation.id === undefined) {
        const today = dayjs().startOf('day');
        // donation.donationDate = today;
      }

      // this.updateForm(donation);

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

  onProvinceChange(): void {
    this.cityService
      .query({
        size: '40',
        'provinceId.equals': this.editForm.get('province')!.value ? this.editForm.get('province')!.value.id : 0,
      })
      .pipe(map((res: HttpResponse<ICity[]>) => res.body ?? []))
      .pipe(map((cities: ICity[]) => this.cityService.addCityToCollectionIfMissing(cities, this.editForm.get('city')!.value)))
      .subscribe((cities: ICity[]) => (this.citiesCollection = cities));
  }

  save(): void {
    this.isSaving = true;
    const report = this.createFromForm();
    this.subscribeToSaveResponse(this.reportService.create(report));
  }

  trackProvinceById(index: number, item: IProvince): number {
    return item.id!;
  }

  trackCityById(index: number, item: ICity): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReport>>): void {
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

  protected updateForm(report: IReport): void {
    this.editForm.patchValue({
      id: report.id,
      isCash: report.isCash,
      amountFrom: report.amountFrom,
      amountTo: report.amountTo,
      fromDate: report.fromDate ? report.fromDate.format(DATE_TIME_FORMAT) : null,
      toDate: report.toDate ? report.toDate.format(DATE_TIME_FORMAT) : null,
      helpType: report.helpType,
      province: report.province,
      city: report.city,
      account: report.account,
    });

    this.provincesCollection = this.provinceService.addProvinceToCollectionIfMissing(this.provincesCollection, report.province);
    this.citiesCollection = this.cityService.addCityToCollectionIfMissing(this.citiesCollection, report.city);
  }

  protected loadRelationshipsOptions(): void {
    this.provinceService
      .query({ size: '32' })
      .pipe(map((res: HttpResponse<IProvince[]>) => res.body ?? []))
      .pipe(
        map((provinces: IProvince[]) =>
          this.provinceService.addProvinceToCollectionIfMissing(provinces, this.editForm.get('province')!.value)
        )
      )
      .subscribe((provinces: IProvince[]) => (this.provincesCollection = provinces));
  }

  protected createFromForm(): IReport {
    const jalaliFromDate = this.editForm.get(['donationDate'])!.value;
    const jalaliToDate = this.editForm.get(['donationDate'])!.value;
    const gregorianFromDate = moment.from(jalaliFromDate.locale('fa').format('YYYY-MM-DD'), 'fa', 'YYYY-MM-DD').format('YYYY-MM-DD');
    const gregorianToDate = moment.from(jalaliToDate.locale('fa').format('YYYY-MM-DD'), 'fa', 'YYYY-MM-DD').format('YYYY-MM-DD');
    return {
      ...new Report(),
      id: this.editForm.get(['id'])!.value,
      isCash: this.editForm.get(['isCash'])!.value,
      amountFrom: this.editForm.get(['amountFrom'])!.value,
      amountTo: this.editForm.get(['amountTo'])!.value,
      fromDate: this.editForm.get(['fromDate'])!.value ? dayjs(gregorianFromDate, DATE_FORMAT) : undefined,
      toDate: this.editForm.get(['toDate'])!.value ? dayjs(gregorianToDate, DATE_FORMAT) : undefined,
      helpType: this.editForm.get(['helpType'])!.value,
      province: this.editForm.get(['province'])!.value,
      city: this.editForm.get(['city'])!.value,
    };
  }
}
