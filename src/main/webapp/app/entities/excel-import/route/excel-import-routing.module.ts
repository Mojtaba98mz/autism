import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ExcelImportComponent } from '../list/excel-import.component';
import { ExcelImportDetailComponent } from '../detail/excel-import-detail.component';
import { ExcelImportUpdateComponent } from '../update/excel-import-update.component';
import { ExcelImportRoutingResolveService } from './excel-import-routing-resolve.service';

const excelImportRoute: Routes = [
  {
    path: '',
    component: ExcelImportComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ExcelImportDetailComponent,
    resolve: {
      excelImport: ExcelImportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ExcelImportUpdateComponent,
    resolve: {
      excelImport: ExcelImportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
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
