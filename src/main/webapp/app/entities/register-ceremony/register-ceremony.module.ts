import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RegisterCeremonyUpdateComponent } from './update/register-ceremony-update.component';
import { RegisterCeremonyRoutingModule } from './route/register-ceremony-routing.module';

@NgModule({
  imports: [SharedModule, RegisterCeremonyRoutingModule],
  declarations: [RegisterCeremonyUpdateComponent],
  entryComponents: [],
})
export class RegisterCeremonyModule {}
