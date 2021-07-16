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
        path: 'donation',
        data: { pageTitle: 'autismApp.donation.home.title' },
        loadChildren: () => import('./donation/donation.module').then(m => m.DonationModule),
      },
      {
        path: 'giver-auditor',
        data: { pageTitle: 'autismApp.giverAuditor.home.title' },
        loadChildren: () => import('./giver-auditor/giver-auditor.module').then(m => m.GiverAuditorModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
