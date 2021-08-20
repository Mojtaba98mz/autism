import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ExcelImportComponent } from '../list/excel-import.component';

const donationRoute: Routes = [
  {
    path: '',
    component: ExcelImportComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(donationRoute)],
  exports: [RouterModule],
})
export class ExcelImportRoutingModule {}
