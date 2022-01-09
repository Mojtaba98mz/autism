import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RegisterDonationUpdateComponent } from './update/register-donation-update.component';
import { RegisterDonationRoutingModule } from './route/register-donation-routing.module';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { DxButtonModule } from 'devextreme-angular';
import { NgxMatSelectSearchModule } from 'ngx-mat-select-search';
import { CommonModule } from '@angular/common';

@NgModule({
  imports: [
    SharedModule,
    RegisterDonationRoutingModule,
    CommonModule,
    MatSelectModule,
    MatFormFieldModule,
    MatIconModule,
    NgxMatSelectSearchModule,
    DxButtonModule,
  ],
  declarations: [RegisterDonationUpdateComponent],
  entryComponents: [],
})
export class RegisterDonationModule {}
