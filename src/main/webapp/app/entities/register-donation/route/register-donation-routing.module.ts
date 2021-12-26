import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RegisterDonationUpdateComponent } from '../update/register-donation-update.component';
const registerDonationRoute: Routes = [
  {
    path: '',
    component: RegisterDonationUpdateComponent,
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(registerDonationRoute)],
  exports: [RouterModule],
})
export class RegisterDonationRoutingModule {}
