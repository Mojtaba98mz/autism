import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { CeremonyUserSelectionService } from '../../core/ceremony-user-selection/ceremony-user-selection.service';
import { ICeremonyUser } from '../../entities/ceremony-user/ceremony-user.model';

@Component({
  selector: 'jhi-ceremony-user-modal',
  templateUrl: './ceremony-user-selection.component.html',
  styleUrls: ['./ceremony-user-selection-modal.scss'],
})
export class CeremonyUserSelectionComponent {
  nameSearch = '';
  familySearch = '';

  constructor(public activeModal: NgbActiveModal, public ceremonyUserSelectionService: CeremonyUserSelectionService) {}

  // eslint-disable-next-line @typescript-eslint/member-ordering
  private _giverList: ICeremonyUser[] | undefined;
  // eslint-disable-next-line @typescript-eslint/member-ordering
  private _selectElement: any;

  cancel(): void {
    this.activeModal.dismiss('cancel');
  }

  /*ngAfterViewInit(): void {
    this.giverSelectionService.giverSelected = undefined;
    this.giverSelectionService.getAllGiver().subscribe(value => {
      if (value.status === 200) {
        if (value.body !== null && value.body !== undefined) {
          this.giverList = value.body;
        }
      }
    });
  }*/

  onEnterPressed(event: any): void {
    if (event.keyCode === 13) {
      this.giverList = [];
      this.ceremonyUserSelectionService.ceremonyUserSelected = undefined;
      this.ceremonyUserSelectionService.getAllCeremonyUser(this.nameSearch + this.familySearch).subscribe(value => {
        if (value.status === 200) {
          if (value.body !== null && value.body !== undefined) {
            this.giverList = value.body;
          }
        }
      });
    }
  }

  trackId(index: number, item: ICeremonyUser): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  selectGiver(item: ICeremonyUser): void {
    this.ceremonyUserSelectionService.ceremonyUserSelected = item;
    this.activeModal.close();
  }

  /* Getter and setter*/

  get giverList(): ICeremonyUser[] | undefined {
    return this._giverList;
  }

  set giverList(value: ICeremonyUser[] | undefined) {
    this._giverList = value;
  }

  get selectElement(): any {
    return this._selectElement;
  }

  set selectElement(value: any) {
    this._selectElement = value;
  }
}
