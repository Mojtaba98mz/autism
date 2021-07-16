import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IGiverAuditor } from '../giver-auditor.model';
import { GiverAuditorService } from '../service/giver-auditor.service';

@Component({
  templateUrl: './giver-auditor-delete-dialog.component.html',
})
export class GiverAuditorDeleteDialogComponent {
  giverAuditor?: IGiverAuditor;

  constructor(protected giverAuditorService: GiverAuditorService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.giverAuditorService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
