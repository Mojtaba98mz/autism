import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { GiverAuditorComponent } from './list/giver-auditor.component';
import { GiverAuditorDetailComponent } from './detail/giver-auditor-detail.component';
import { GiverAuditorUpdateComponent } from './update/giver-auditor-update.component';
import { GiverAuditorDeleteDialogComponent } from './delete/giver-auditor-delete-dialog.component';
import { GiverAuditorRoutingModule } from './route/giver-auditor-routing.module';

@NgModule({
  imports: [SharedModule, GiverAuditorRoutingModule],
  declarations: [GiverAuditorComponent, GiverAuditorDetailComponent, GiverAuditorUpdateComponent, GiverAuditorDeleteDialogComponent],
  entryComponents: [GiverAuditorDeleteDialogComponent],
})
export class GiverAuditorModule {}
