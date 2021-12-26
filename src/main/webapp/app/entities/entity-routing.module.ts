import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'giver',
        data: { pageTitle: 'autismApp.giver.home.title' },
        loadChildren: () => import('./giver/giver.module').then(m => m.GiverModule),
      },
      {
        path: 'giver-assigner',
        data: { pageTitle: 'autismApp.giverAssigner.home.title' },
        loadChildren: () => import('./giver-assigner/giver-assigner.module').then(m => m.GiverAssignerModule),
      },
      {
        path: 'excel-import',
        data: { pageTitle: 'autismApp.excelImport.home.title' },
        loadChildren: () => import('./excel-import/excel-import.module').then(m => m.ExcelImportModule),
      },
      {
        path: 'donation',
        data: { pageTitle: 'autismApp.donation.home.title' },
        loadChildren: () => import('./donation/donation.module').then(m => m.DonationModule),
      },
      {
        path: 'giver-auditor',
        data: { pageTitle: 'autismApp.giverAuditor.home.title' },
        loadChildren: () => import('./giver-auditor/giver-auditor.module').then(m => m.GiverAuditorModule),
      },
      {
        path: 'province',
        data: { pageTitle: 'autismApp.province.home.title' },
        loadChildren: () => import('./province/province.module').then(m => m.ProvinceModule),
      },
      {
        path: 'city',
        data: { pageTitle: 'autismApp.city.home.title' },
        loadChildren: () => import('./city/city.module').then(m => m.CityModule),
      },
      {
        path: 'register-donation',
        data: { pageTitle: 'autismApp.registerDonation.home.title' },
        loadChildren: () => import('./register-donation/register-donation.module').then(m => m.RegisterDonationModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
