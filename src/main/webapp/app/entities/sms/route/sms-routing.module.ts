import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SmsUpdateComponent } from '../update/sms-update.component';
const smsRoute: Routes = [
  {
    path: '',
    component: SmsUpdateComponent,
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(smsRoute)],
  exports: [RouterModule],
})
export class SmsRoutingModule {}
