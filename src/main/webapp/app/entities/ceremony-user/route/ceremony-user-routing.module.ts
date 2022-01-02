import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CeremonyUserComponent } from '../list/ceremony-user.component';
import { CeremonyUserDetailComponent } from '../detail/ceremony-user-detail.component';
import { CeremonyUserUpdateComponent } from '../update/ceremony-user-update.component';
import { CeremonyUserRoutingResolveService } from './ceremony-user-routing-resolve.service';

const ceremonyUserRoute: Routes = [
  {
    path: '',
    component: CeremonyUserComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CeremonyUserDetailComponent,
    resolve: {
      ceremonyUser: CeremonyUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CeremonyUserUpdateComponent,
    resolve: {
      ceremonyUser: CeremonyUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CeremonyUserUpdateComponent,
    resolve: {
      ceremonyUser: CeremonyUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(ceremonyUserRoute)],
  exports: [RouterModule],
})
export class CeremonyUserRoutingModule {}
