import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CeremonyComponent } from './list/ceremony.component';
import { CeremonyDetailComponent } from './detail/ceremony-detail.component';
import { CeremonyUpdateComponent } from './update/ceremony-update.component';
import { CeremonyDeleteDialogComponent } from './delete/ceremony-delete-dialog.component';
import { CeremonyRoutingModule } from './route/ceremony-routing.module';

@NgModule({
  imports: [SharedModule, CeremonyRoutingModule],
  declarations: [CeremonyComponent, CeremonyDetailComponent, CeremonyUpdateComponent, CeremonyDeleteDialogComponent],
  entryComponents: [CeremonyDeleteDialogComponent],
})
export class CeremonyModule {}
