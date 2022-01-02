import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CeremonyUserComponent } from './list/ceremony-user.component';
import { CeremonyUserDetailComponent } from './detail/ceremony-user-detail.component';
import { CeremonyUserUpdateComponent } from './update/ceremony-user-update.component';
import { CeremonyUserDeleteDialogComponent } from './delete/ceremony-user-delete-dialog.component';
import { CeremonyUserRoutingModule } from './route/ceremony-user-routing.module';

@NgModule({
  imports: [SharedModule, CeremonyUserRoutingModule],
  declarations: [CeremonyUserComponent, CeremonyUserDetailComponent, CeremonyUserUpdateComponent, CeremonyUserDeleteDialogComponent],
  entryComponents: [CeremonyUserDeleteDialogComponent],
})
export class CeremonyUserModule {}
