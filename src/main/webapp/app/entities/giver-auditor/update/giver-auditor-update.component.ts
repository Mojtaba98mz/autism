import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IGiverAuditor, GiverAuditor } from '../giver-auditor.model';
import { GiverAuditorService } from '../service/giver-auditor.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IGiver } from 'app/entities/giver/giver.model';
import { GiverService } from 'app/entities/giver/service/giver.service';

@Component({
  selector: 'jhi-giver-auditor-update',
  templateUrl: './giver-auditor-update.component.html',
})
export class GiverAuditorUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  giversSharedCollection: IGiver[] = [];

  editForm = this.fb.group({
    id: [],
    fiedlName: [null, [Validators.required]],
    oldValue: [null, [Validators.required]],
    newValue: [null, [Validators.required]],
    changeDate: [null, [Validators.required]],
    auditor: [],
    giver: [],
  });

  constructor(
    protected giverAuditorService: GiverAuditorService,
    protected userService: UserService,
    protected giverService: GiverService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ giverAuditor }) => {
      if (giverAuditor.id === undefined) {
        const today = dayjs().startOf('day');
        giverAuditor.changeDate = today;
      }

      this.updateForm(giverAuditor);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const giverAuditor = this.createFromForm();
    if (giverAuditor.id !== undefined) {
      this.subscribeToSaveResponse(this.giverAuditorService.update(giverAuditor));
    } else {
      this.subscribeToSaveResponse(this.giverAuditorService.create(giverAuditor));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackGiverById(index: number, item: IGiver): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGiverAuditor>>): void {
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

  protected updateForm(giverAuditor: IGiverAuditor): void {
    this.editForm.patchValue({
      id: giverAuditor.id,
      fiedlName: giverAuditor.fiedlName,
      oldValue: giverAuditor.oldValue,
      newValue: giverAuditor.newValue,
      changeDate: giverAuditor.changeDate ? giverAuditor.changeDate.format(DATE_TIME_FORMAT) : null,
      auditor: giverAuditor.auditor,
      giver: giverAuditor.giver,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, giverAuditor.auditor);
    this.giversSharedCollection = this.giverService.addGiverToCollectionIfMissing(this.giversSharedCollection, giverAuditor.giver);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('auditor')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.giverService
      .query()
      .pipe(map((res: HttpResponse<IGiver[]>) => res.body ?? []))
      .pipe(map((givers: IGiver[]) => this.giverService.addGiverToCollectionIfMissing(givers, this.editForm.get('giver')!.value)))
      .subscribe((givers: IGiver[]) => (this.giversSharedCollection = givers));
  }

  protected createFromForm(): IGiverAuditor {
    return {
      ...new GiverAuditor(),
      id: this.editForm.get(['id'])!.value,
      fiedlName: this.editForm.get(['fiedlName'])!.value,
      oldValue: this.editForm.get(['oldValue'])!.value,
      newValue: this.editForm.get(['newValue'])!.value,
      changeDate: this.editForm.get(['changeDate'])!.value ? dayjs(this.editForm.get(['changeDate'])!.value, DATE_TIME_FORMAT) : undefined,
      auditor: this.editForm.get(['auditor'])!.value,
      giver: this.editForm.get(['giver'])!.value,
    };
  }
}
