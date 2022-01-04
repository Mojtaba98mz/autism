import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RegisterDonationUpdateComponent } from './update/register-donation-update.component';
import { RegisterDonationRoutingModule } from './route/register-donation-routing.module';
import { MatSelectModule } from '@angular/material/select';
import { MatIconModule } from '@angular/material/icon';

@NgModule({
  imports: [SharedModule, RegisterDonationRoutingModule, MatSelectModule, MatIconModule],
  declarations: [RegisterDonationUpdateComponent],
  entryComponents: [],
})
export class RegisterDonationModule {}
