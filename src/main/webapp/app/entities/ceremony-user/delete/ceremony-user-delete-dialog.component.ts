import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICeremonyUser } from '../ceremony-user.model';
import { CeremonyUserService } from '../service/ceremony-user.service';

@Component({
  templateUrl: './ceremony-user-delete-dialog.component.html',
})
export class CeremonyUserDeleteDialogComponent {
  ceremonyUser?: ICeremonyUser;

  constructor(protected ceremonyUserService: CeremonyUserService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ceremonyUserService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
