import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IGiver } from 'app/entities/giver/giver.model';
import { IUser } from '../../entities/user/user.model';
import { UserSelectionService } from '../../core/user-selection/user-selection.service';

@Component({
  selector: 'jhi-user-modal',
  templateUrl: './user-selection.component.html',
  styleUrls: ['./user-selection-modal.scss'],
})
export class UserSelectionComponent {
  nameSearch = '';
  familySearch = '';

  constructor(public activeModal: NgbActiveModal, public userSelectionService: UserSelectionService) {}

  // eslint-disable-next-line @typescript-eslint/member-ordering
  private _userList: IUser[] | undefined;
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
      this.userList = [];
      this.userSelectionService.selected = undefined;
      this.userSelectionService.getAll(this.nameSearch + this.familySearch).subscribe(value => {
        if (value.status === 200) {
          if (value.body !== null && value.body !== undefined) {
            this.userList = value.body;
          }
        }
      });
    }
  }

  trackId(index: number, item: IGiver): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  selectUser(item: IUser): void {
    this.userSelectionService.selected = item;
    this.activeModal.close();
  }

  /* Getter and setter*/

  get userList(): IUser[] | undefined {
    return this._userList;
  }

  set userList(value: IUser[] | undefined) {
    this._userList = value;
  }

  get selectElement(): any {
    return this._selectElement;
  }

  set selectElement(value: any) {
    this._selectElement = value;
  }
}
