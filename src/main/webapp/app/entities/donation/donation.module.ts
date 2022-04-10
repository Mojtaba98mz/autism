import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DonationComponent } from './list/donation.component';
import { DonationDetailComponent } from './detail/donation-detail.component';
import { DonationUpdateComponent } from './update/donation-update.component';
import { DonationDeleteDialogComponent } from './delete/donation-delete-dialog.component';
import { DonationRoutingModule } from './route/donation-routing.module';
import { DpDatePickerModule } from 'ng2-jalali-date-picker';

@NgModule({
  imports: [SharedModule, DonationRoutingModule, DpDatePickerModule],
  declarations: [DonationComponent, DonationDetailComponent, DonationUpdateComponent, DonationDeleteDialogComponent],
  entryComponents: [DonationDeleteDialogComponent],
})
export class DonationModule {}
