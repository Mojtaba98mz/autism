import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { GiverAssignerComponent } from '../list/giver-assigner.component';

const giverRoute: Routes = [
  {
    path: '',
    component: GiverAssignerComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(giverRoute)],
  exports: [RouterModule],
})
export class GiverAssignerRoutingModule {}
