import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ApplicationConfigService } from 'app/core/config/application-config.service';

@Injectable({ providedIn: 'root' })
export class DonationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/donations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}
}
