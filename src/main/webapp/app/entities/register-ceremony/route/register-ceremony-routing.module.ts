import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RegisterCeremonyUpdateComponent } from '../update/register-ceremony-update.component';

const ceremonyRoute: Routes = [
  {
    path: '',
    component: RegisterCeremonyUpdateComponent,
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(ceremonyRoute)],
  exports: [RouterModule],
})
export class RegisterCeremonyRoutingModule {}
