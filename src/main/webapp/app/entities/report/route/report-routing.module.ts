import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ReportUpdateComponent } from '../update/report-update.component';
const reportRoute: Routes = [
  {
    path: '',
    component: ReportUpdateComponent,
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(reportRoute)],
  exports: [RouterModule],
})
export class ReportRoutingModule {}
