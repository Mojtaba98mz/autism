import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICeremony } from '../ceremony.model';
import { CeremonyService } from '../service/ceremony.service';

@Component({
  templateUrl: './ceremony-delete-dialog.component.html',
})
export class CeremonyDeleteDialogComponent {
  ceremony?: ICeremony;

  constructor(protected ceremonyService: CeremonyService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ceremonyService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
