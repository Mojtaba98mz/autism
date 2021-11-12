import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IGiver, Giver } from '../giver.model';
import { GiverService } from '../service/giver.service';
import { IProvince } from 'app/entities/province/province.model';
import { ProvinceService } from 'app/entities/province/service/province.service';
import { ICity } from 'app/entities/city/city.model';
import { CityService } from 'app/entities/city/service/city.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-giver-update',
  templateUrl: './giver-update.component.html',
})
export class GiverUpdateComponent implements OnInit {
  isSaving = false;

  provincesCollection: IProvince[] = [];
  citiesCollection: ICity[] = [];
  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    family: [null, [Validators.required]],
    phoneNumber: [null, [Validators.required, Validators.pattern('^(\\+98|0098|98|0)?9\\d{9}$')]],
    homeNumber: [null, [Validators.pattern('^[0-9]\\d*$')]],
    address: [],
    absorbDate: [],
    province: [],
    city: [],
    absorbant: [],
    supporter: [],
  });

  constructor(
    protected giverService: GiverService,
    protected provinceService: ProvinceService,
    protected cityService: CityService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ giver }) => {
      if (giver.id === undefined) {
        const today = dayjs().startOf('day');
        giver.absorbDate = today;
      }

      this.updateForm(giver);

      this.loadRelationshipsOptions();
    });
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
    const giver = this.createFromForm();
    if (giver.id !== undefined) {
      this.subscribeToSaveResponse(this.giverService.update(giver));
    } else {
      this.subscribeToSaveResponse(this.giverService.create(giver));
    }
  }

  trackProvinceById(index: number, item: IProvince): number {
    return item.id!;
  }

  trackCityById(index: number, item: ICity): number {
    return item.id!;
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGiver>>): void {
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

  protected updateForm(giver: IGiver): void {
    this.editForm.patchValue({
      id: giver.id,
      name: giver.name,
      family: giver.family,
      phoneNumber: giver.phoneNumber,
      homeNumber: giver.homeNumber,
      address: giver.address,
      absorbDate: giver.absorbDate ? giver.absorbDate.format(DATE_TIME_FORMAT) : null,
      province: giver.province,
      city: giver.city,
      absorbant: giver.absorbant,
      supporter: giver.supporter,
    });

    this.provincesCollection = this.provinceService.addProvinceToCollectionIfMissing(this.provincesCollection, giver.province);
    this.citiesCollection = this.cityService.addCityToCollectionIfMissing(this.citiesCollection, giver.city);
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      giver.absorbant,
      giver.supporter
    );
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

    /* this.cityService
      .query({
        'size': '40',
        'provinceId.equals': this.editForm.get('province')!.value ? this.editForm.get('province')!.value.id : 0
      })
      .pipe(map((res: HttpResponse<ICity[]>) => res.body ?? []))
      .pipe(map((cities: ICity[]) => this.cityService.addCityToCollectionIfMissing(cities, this.editForm.get('city')!.value)))
      .subscribe((cities: ICity[]) => (this.citiesCollection = cities));*/

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(
        map((users: IUser[]) =>
          this.userService.addUserToCollectionIfMissing(users, this.editForm.get('absorbant')!.value, this.editForm.get('supporter')!.value)
        )
      )
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IGiver {
    return {
      ...new Giver(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      family: this.editForm.get(['family'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      homeNumber: this.editForm.get(['homeNumber'])!.value,
      address: this.editForm.get(['address'])!.value,
      absorbDate: this.editForm.get(['absorbDate'])!.value ? dayjs(this.editForm.get(['absorbDate'])!.value, DATE_TIME_FORMAT) : undefined,
      province: this.editForm.get(['province'])!.value,
      city: this.editForm.get(['city'])!.value,
      absorbant: this.editForm.get(['absorbant'])!.value,
      supporter: this.editForm.get(['supporter'])!.value,
    };
  }
}
