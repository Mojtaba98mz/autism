import { AfterViewInit, Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { GiverSelectionService } from 'app/core/giver-selection/giver-selection.service';
import { IGiver } from 'app/entities/giver/giver.model';

@Component({
  selector: 'jhi-giver-modal',
  templateUrl: './giver-selection.component.html',
  styleUrls: ['./giver-selection-modal.scss'],
})
export class GiverSelectionComponent {
  nameSearch = '';
  familySearch = '';

  constructor(public activeModal: NgbActiveModal, public giverSelectionService: GiverSelectionService) {}

  // eslint-disable-next-line @typescript-eslint/member-ordering
  private _giverList: IGiver[] | undefined;
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
      this.giverSelectionService.giverSelected = undefined;
      this.giverSelectionService.getAllGiver(this.nameSearch + this.familySearch).subscribe(value => {
        if (value.status === 200) {
          if (value.body !== null && value.body !== undefined) {
            this.giverList = value.body;
          }
        }
      });
    }
  }

  trackId(index: number, item: IGiver): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  selectGiver(item: IGiver): void {
    this.giverSelectionService.giverSelected = item;
    this.activeModal.close();
  }

  /* Getter and setter*/

  get giverList(): IGiver[] | undefined {
    return this._giverList;
  }

  set giverList(value: IGiver[] | undefined) {
    this._giverList = value;
  }

  get selectElement(): any {
    return this._selectElement;
  }

  set selectElement(value: any) {
    this._selectElement = value;
  }
}
