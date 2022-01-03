import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CeremonyComponent } from '../list/ceremony.component';
import { CeremonyDetailComponent } from '../detail/ceremony-detail.component';
import { CeremonyUpdateComponent } from '../update/ceremony-update.component';
import { CeremonyRoutingResolveService } from './ceremony-routing-resolve.service';
import { DonationComponent } from '../../donation/list/donation.component';

const ceremonyRoute: Routes = [
  {
    path: '',
    component: CeremonyComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CeremonyDetailComponent,
    resolve: {
      ceremony: CeremonyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':ceremonyUserId/new',
    component: CeremonyUpdateComponent,
    resolve: {
      ceremony: CeremonyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/:ceremonyUserId/edit',
    component: CeremonyUpdateComponent,
    resolve: {
      ceremony: CeremonyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':ceremonyUserId/viewCeremony',
    component: CeremonyComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(ceremonyRoute)],
  exports: [RouterModule],
})
export class CeremonyRoutingModule {}
