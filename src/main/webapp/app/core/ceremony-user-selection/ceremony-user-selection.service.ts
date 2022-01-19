import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { IGiver } from 'app/entities/giver/giver.model';
import { SERVER_API_URL } from 'app/app.constants';
import { ICeremonyUser } from '../../entities/ceremony-user/ceremony-user.model';

@Injectable({ providedIn: 'root' })
export class CeremonyUserSelectionService {
  public resourceUrl = SERVER_API_URL + '/api/ceremony-users';
  private _ceremonyUserSelected: ICeremonyUser | undefined;
  constructor(private http: HttpClient) {}

  getAllCeremonyUser(nameFamily: string): Observable<HttpResponse<IGiver[]>> {
    return this.http.get<IGiver[]>(this.resourceUrl + '/filter-by-name?filter=' + nameFamily, { observe: 'response' });
  }

  // GETTER AND SETTER
  get ceremonyUserSelected(): ICeremonyUser | undefined {
    return this._ceremonyUserSelected;
  }

  set ceremonyUserSelected(value: ICeremonyUser | undefined) {
    this._ceremonyUserSelected = value;
  }
}
