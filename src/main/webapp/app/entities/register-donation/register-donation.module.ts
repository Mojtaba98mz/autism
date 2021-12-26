import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RegisterDonationUpdateComponent } from './update/register-donation-update.component';
import { RegisterDonationRoutingModule } from './route/register-donation-routing.module';

@NgModule({
  imports: [SharedModule, RegisterDonationRoutingModule],
  declarations: [RegisterDonationUpdateComponent],
  entryComponents: [],
})
export class RegisterDonationModule {}
