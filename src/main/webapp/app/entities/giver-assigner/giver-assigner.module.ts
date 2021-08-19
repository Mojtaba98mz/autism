import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { GiverAssignerComponent } from './list/giver-assigner.component';
import { GiverAssignerRoutingModule } from './route/giver-assigner-routing.module';

@NgModule({
  imports: [SharedModule, GiverAssignerRoutingModule],
  declarations: [GiverAssignerComponent],
})
export class GiverAssignerModule {}
