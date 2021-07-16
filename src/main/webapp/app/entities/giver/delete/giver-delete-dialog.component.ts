import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IGiver } from '../giver.model';
import { GiverService } from '../service/giver.service';

@Component({
  templateUrl: './giver-delete-dialog.component.html',
})
export class GiverDeleteDialogComponent {
  giver?: IGiver;

  constructor(protected giverService: GiverService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.giverService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
