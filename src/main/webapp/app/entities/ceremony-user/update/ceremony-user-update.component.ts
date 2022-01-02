import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICeremonyUser, CeremonyUser } from '../ceremony-user.model';
import { CeremonyUserService } from '../service/ceremony-user.service';

@Component({
  selector: 'jhi-ceremony-user-update',
  templateUrl: './ceremony-user-update.component.html',
})
export class CeremonyUserUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    family: [null, [Validators.required]],
    phoneNumber: [null, [Validators.required]],
    homeNumber: [],
    address: [],
  });

  constructor(protected ceremonyUserService: CeremonyUserService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ceremonyUser }) => {
      this.updateForm(ceremonyUser);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ceremonyUser = this.createFromForm();
    if (ceremonyUser.id !== undefined) {
      this.subscribeToSaveResponse(this.ceremonyUserService.update(ceremonyUser));
    } else {
      this.subscribeToSaveResponse(this.ceremonyUserService.create(ceremonyUser));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICeremonyUser>>): void {
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

  protected updateForm(ceremonyUser: ICeremonyUser): void {
    this.editForm.patchValue({
      id: ceremonyUser.id,
      name: ceremonyUser.name,
      family: ceremonyUser.family,
      phoneNumber: ceremonyUser.phoneNumber,
      homeNumber: ceremonyUser.homeNumber,
      address: ceremonyUser.address,
    });
  }

  protected createFromForm(): ICeremonyUser {
    return {
      ...new CeremonyUser(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      family: this.editForm.get(['family'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      homeNumber: this.editForm.get(['homeNumber'])!.value,
      address: this.editForm.get(['address'])!.value,
    };
  }
}
