import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { GiverComponent } from '../list/giver.component';
import { GiverDetailComponent } from '../detail/giver-detail.component';
import { GiverUpdateComponent } from '../update/giver-update.component';
import { GiverRoutingResolveService } from './giver-routing-resolve.service';

const giverRoute: Routes = [
  {
    path: '',
    component: GiverComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GiverDetailComponent,
    resolve: {
      giver: GiverRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GiverUpdateComponent,
    resolve: {
      giver: GiverRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GiverUpdateComponent,
    resolve: {
      giver: GiverRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(giverRoute)],
  exports: [RouterModule],
})
export class GiverRoutingModule {}
