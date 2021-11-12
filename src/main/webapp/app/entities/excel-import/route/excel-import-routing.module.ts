import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ExcelImportUpdateComponent } from '../update/excel-import-update.component';
import { ExcelImportRoutingResolveService } from './excel-import-routing-resolve.service';

const excelImportRoute: Routes = [
  {
    path: '',
    component: ExcelImportUpdateComponent,
    resolve: {
      excelImport: ExcelImportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(excelImportRoute)],
  exports: [RouterModule],
})
export class ExcelImportRoutingModule {}
