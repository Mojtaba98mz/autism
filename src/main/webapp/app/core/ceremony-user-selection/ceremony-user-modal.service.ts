import { Injectable } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

import { GiverSelectionComponent } from 'app/shared/giver-modal/giver-selection.component';
import { CeremonyUserSelectionComponent } from '../../shared/ceremony-user-modal/ceremony-user-selection.component';

@Injectable({ providedIn: 'root' })
export class CeremonyUserModalService {
  constructor(private modalService: NgbModal) {}
  open(): NgbModalRef {
    return this.modalService.open(CeremonyUserSelectionComponent);
  }
}
