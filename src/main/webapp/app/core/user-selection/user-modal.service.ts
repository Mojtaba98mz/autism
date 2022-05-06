import { Injectable } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

import { UserSelectionComponent } from '../../shared/user-modal/user-selection.component';

@Injectable({ providedIn: 'root' })
export class UserModalService {
  constructor(private modalService: NgbModal) {}
  open(): NgbModalRef {
    return this.modalService.open(UserSelectionComponent);
  }
}
