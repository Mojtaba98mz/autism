import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { GiverComponent } from './list/giver.component';
import { GiverDetailComponent } from './detail/giver-detail.component';
import { GiverUpdateComponent } from './update/giver-update.component';
import { GiverDeleteDialogComponent } from './delete/giver-delete-dialog.component';
import { GiverRoutingModule } from './route/giver-routing.module';

@NgModule({
  imports: [SharedModule, GiverRoutingModule],
  declarations: [GiverComponent, GiverDetailComponent, GiverUpdateComponent, GiverDeleteDialogComponent],
  entryComponents: [GiverDeleteDialogComponent],
})
export class GiverModule {}
