import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ApplicationConfigService } from 'app/core/config/application-config.service';

@Injectable({ providedIn: 'root' })
export class SmsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/reports');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  sendMessage(content: string): void {
    this.http.get<void>(this.resourceUrl + `/send/${content}`, { observe: 'response' }).subscribe();
  }
}
