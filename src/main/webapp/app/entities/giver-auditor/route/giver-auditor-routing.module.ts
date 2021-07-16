import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { GiverAuditorComponent } from '../list/giver-auditor.component';
import { GiverAuditorDetailComponent } from '../detail/giver-auditor-detail.component';
import { GiverAuditorUpdateComponent } from '../update/giver-auditor-update.component';
import { GiverAuditorRoutingResolveService } from './giver-auditor-routing-resolve.service';

const giverAuditorRoute: Routes = [
  {
    path: '',
    component: GiverAuditorComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GiverAuditorDetailComponent,
    resolve: {
      giverAuditor: GiverAuditorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GiverAuditorUpdateComponent,
    resolve: {
      giverAuditor: GiverAuditorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GiverAuditorUpdateComponent,
    resolve: {
      giverAuditor: GiverAuditorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(giverAuditorRoute)],
  exports: [RouterModule],
})
export class GiverAuditorRoutingModule {}
