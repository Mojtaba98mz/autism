import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { IGiver } from 'app/entities/giver/giver.model';
import { SERVER_API_URL } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class GiverSelectionService {
  public resourceUrl = SERVER_API_URL + '/api/givers';
  private _giverSelected: IGiver | undefined;
  constructor(private http: HttpClient) {}

  getAllGiver(nameFamily: string): Observable<HttpResponse<IGiver[]>> {
    return this.http.get<IGiver[]>(this.resourceUrl + '/filter-by-giver-name?filter=' + nameFamily, { observe: 'response' });
  }

  // GETTER AND SETTER
  get giverSelected(): IGiver | undefined {
    return this._giverSelected;
  }

  set giverSelected(value: IGiver | undefined) {
    this._giverSelected = value;
  }
}
